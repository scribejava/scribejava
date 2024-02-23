package com.github.scribejava.apis.esia;

import com.github.scribejava.core.base64.Base64;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.exceptions.OAuthSignatureException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import static java.util.Objects.requireNonNull;

public class ECGOST2012SigningService implements SigningService {
  protected static final String KEY_ALGORITHM = "ECGOST3410-2012";
  protected static final String SIGNATURE_ALGORITHM = "GOST3411-2012-256WITHGOST3410-2012-256";
  private final X509CertificateHolder certificate;
  private final PrivateKey privateKey;

  protected static class ProviderInstanceHolder {
    private static final BouncyCastleProvider BC_PROVIDER;
    static {
      final Provider bcProvider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
      if (bcProvider == null) {
        BC_PROVIDER  = new BouncyCastleProvider();
        Security.addProvider(BC_PROVIDER);
      } else {
        if (bcProvider instanceof BouncyCastleProvider) {
          BC_PROVIDER = (BouncyCastleProvider) bcProvider;
        } else {
          throw new IllegalStateException("Security provider with name \"" + BouncyCastleProvider.PROVIDER_NAME +
                  "\" is already installed and it is not " + BouncyCastleProvider.class.getCanonicalName() +
                  " but " + bcProvider.getClass().getCanonicalName());
        }
      }
    }
  }

  public ECGOST2012SigningService(X509Certificate certificate, PrivateKey privateKey)
          throws CertificateEncodingException {
    this(new JcaX509CertificateHolder(certificate), privateKey);
  }

  public ECGOST2012SigningService(X509CertificateHolder certificate, PrivateKey privateKey) {
    this.certificate = requireNonNull(certificate);
    this.privateKey = requireNonNull(privateKey);
  }

  public static ECGOST2012SigningService usingPemFiles(String certificatePemFile, String privateKeyPemFile) {
    final X509CertificateHolder certificateHolder;
    final PrivateKeyInfo keyInfo;
    try {
      certificateHolder = readPemFile(certificatePemFile);
      keyInfo = readPemFile(privateKeyPemFile);
    } catch (Exception e) {
      throw new OAuthException("Cannot read PEM file", e);
    }
    final PrivateKey key;
    try {
      key = convertPrivateKey(keyInfo);
    } catch (Exception e) {
      throw new OAuthException("Cannot convert private key", e);
    }
    return new ECGOST2012SigningService(certificateHolder, key);
  }

  @Override
  public String createSignature(String stringToSign) throws OAuthSignatureException {
    final byte[] dataToSign = stringToSign.getBytes(StandardCharsets.UTF_8);
    final byte[] signatureBytes;
    try {
      signatureBytes = createPKCS7(dataToSign, certificate, privateKey, false, true);
    } catch (Exception e) {
      throw new OAuthSignatureException(stringToSign, e);
    }
    return Base64.encode(signatureBytes);
  }

  @SuppressWarnings("unchecked")
  protected static <T> T readPemFile(String fileName) throws IOException {
    return (T) new PEMParser(new FileReader(fileName))
        .readObject();
  }

  public X509CertificateHolder getCertificate() {
    return certificate;
  }

  protected PrivateKey getPrivateKey() {
    return privateKey;
  }

  protected static PrivateKey convertPrivateKey(PrivateKeyInfo keyInfo) throws NoSuchAlgorithmException,
          IOException, InvalidKeySpecException {
    return KeyFactory.getInstance(KEY_ALGORITHM, ProviderInstanceHolder.BC_PROVIDER)
        .generatePrivate(new PKCS8EncodedKeySpec(keyInfo.getEncoded()));
  }

  protected static byte[] createPKCS7(byte[] data, X509CertificateHolder signingCertificate, PrivateKey signingKey,
          boolean includeData, boolean includeCertificate)
          throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
    final CMSTypedData cmsData = new CMSProcessableByteArray(data);

    final CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
    final ContentSigner contentSigner = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(signingKey);
    cmsGenerator.addSignerInfoGenerator(
        new JcaSignerInfoGeneratorBuilder(
            new JcaDigestCalculatorProviderBuilder().setProvider(ProviderInstanceHolder.BC_PROVIDER).build())
            .build(contentSigner, signingCertificate));
    // next ESIA API version might allow or even require to exclude signer certificate from PKCS#7 signature
    if (includeCertificate) {
      cmsGenerator.addCertificates(new JcaCertStore(Collections.singleton(signingCertificate)));
    }

    final CMSSignedData cms = cmsGenerator.generate(cmsData, includeData);
    return cms.getEncoded();
  }
}
