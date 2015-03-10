package com.onegini.actions;

import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_AUTHENTICATION_ERROR;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_DEVICE_ALREADY_ENROLLED;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_ERROR;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_INVALID_CREDENTIALS;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_INVALID_REQUEST;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_NOT_AVAILABLE;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_SUCCESS;
import static com.onegini.responses.MobileAuthEnrollmentResponse.ENROLLMENT_USER_ALREADY_ENROLLED;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.OneginiCordovaPlugin;
import com.onegini.gcm.GCMHelper;
import com.onegini.mobile.sdk.android.library.handlers.OneginiMobileAuthEnrollmentHandler;
import com.onegini.resource.ResourceRequest;
import com.onegini.util.CallbackResultBuilder;

public class MobileAuthenticationAction implements OneginiPluginAction {

  private CallbackResultBuilder callbackResultBuilder;

  public MobileAuthenticationAction() {
    callbackResultBuilder = new CallbackResultBuilder();
  }

  @Override
  public void execute(final JSONArray args, final CallbackContext callbackContext, final OneginiCordovaPlugin client) {
    if (args.length() != 1) {
      callbackContext.error("Invalid parameter, expected 1, got " + args.length() + ".");
      return;
    }

    try {
      final String[] scopes = ResourceRequest.parseScopes(args.getJSONArray(0));
      enroll(scopes, callbackContext, client);
    } catch (JSONException e) {
      callbackContext.error("Invalid parameter, failed to read scopes," + e.getMessage());
    }
  }

  private void enroll(final String[] scopes, final CallbackContext callbackContext, final OneginiCordovaPlugin client) {
    final GCMHelper gcmHelper = new GCMHelper(client.getCordova().getActivity().getApplicationContext());
    gcmHelper.registerGCMService(client.getOneginiClient(), scopes, buildEnrollHandlerForCallback(callbackContext));
  }

  private OneginiMobileAuthEnrollmentHandler buildEnrollHandlerForCallback(final CallbackContext callbackContext) {
    return new OneginiMobileAuthEnrollmentHandler() {
      @Override
      public void enrollmentSuccess() {
        callbackContext.success(ENROLLMENT_SUCCESS.getName());
      }

      @Override
      public void enrollmentError() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_ERROR.getName())
                .build());
      }

      @Override
      public void enrollmentAuthenticationError() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_AUTHENTICATION_ERROR.getName())
                .build());
      }

      @Override
      public void enrollmentException(final Exception exception) {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_ERROR.getName())
                .build());
      }

      @Override
      public void enrollmentNotAvailable() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_NOT_AVAILABLE.getName())
                .build());
      }

      @Override
      public void enrollmentInvalidRequest() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_INVALID_REQUEST.getName())
                .build());
      }

      @Override
      public void enrollmentInvalidClientCredentials() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_INVALID_CREDENTIALS.getName())
                .build());
      }

      @Override
      public void enrollmentDeviceAlreadyEnrolled() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_DEVICE_ALREADY_ENROLLED.getName())
                .build());
      }

      @Override
      public void enrollmentUserAlreadyEnrolled() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_USER_ALREADY_ENROLLED.getName())
                .build());
      }

      @Override
      public void enrollmentInvalidTransaction() {
        callbackContext.sendPluginResult(
            callbackResultBuilder
                .withErrorReason(ENROLLMENT_ERROR.getName())
                .build());
      }
    };
  }
}
