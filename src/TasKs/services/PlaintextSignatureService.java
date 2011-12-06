package TasKs.services;

import TasKs.exceptions.*;
import TasKs.utils.*;

/**
 * plaintext implementation of {@SignatureService}
 *
 * @author Pablo Fernandez
 *
 */
public class PlaintextSignatureService implements SignatureService
{
  private static final String METHOD = "plaintext";

  /**
   * {@inheritDoc}
   */
  public String getSignature(String baseString, String apiSecret, String tokenSecret)
  {
    try
    {
      Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
      return URLUtils.percentEncode(apiSecret) + '&' + URLUtils.percentEncode(tokenSecret);
    }
    catch (Exception e)
    {
      throw new OAuthSignatureException(baseString, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getSignatureMethod()
  {
    return METHOD;
  }
}

