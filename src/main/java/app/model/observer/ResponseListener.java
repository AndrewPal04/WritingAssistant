package app.model.observer;

import app.model.WritingResponse;

/**
 * Observer/listener interface for async responses.
 */
public interface ResponseListener {

    void onSuccess(WritingResponse response);

    void onError(String errorMessage);
}
