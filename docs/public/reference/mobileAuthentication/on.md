# Handling mobile authentication requests

<!-- toc -->

When a user is enrolled for mobile authentication, they are able to receive and respond to mobile authentication requests. The Onegini Cordova Plugin facilitates this by allowing you to register handler methods that implement the UI.

## `onegini.mobileAuthentication.on`

This method is used to register handlers for different mobile authentication methods. The various methods (**push**, **push with PIN**, etc.) can be [configured](https://docs.onegini.com/token-server/topics/mobile-apps/mobile-authentication/mobile-authentication.html#configure-authentication-properties) in the Token Server. Once configured, the handlers for these various methods need to be registered using `on`, which takes the method string as parameter.

| Parameter | Description |
| --- | --- |
| `method` | The mobile authentication method you wish to register a handler for

Currently, the available method strings are `"confirmation"` (push), `"pin"` (push with PIN), and `"fingerprint"` (push with fingerprint).

The `on` method returns a [`MobileAuthenticationHandler`](MobileAuthenticationHandler.md) object, which can be used to implement the various handler methods.
Examples for implementing Mobile Authentication request can be found in the Mobile Authentication [topic guide](../../topics/mobile-authentication.md).