package TasKs.builder.api;

import TasKs.model.*;

public class ConstantContactApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://oauth.constantcontact.com/ws/oauth/confirm_access?oauth_token=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://oauth.constantcontact.com/ws/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://oauth.constantcontact.com/ws/oauth/request_token";
  }
}
