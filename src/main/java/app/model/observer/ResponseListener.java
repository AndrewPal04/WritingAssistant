package app.model.observer;

import app.model.WritingResponse;

public interface ResponseListener {

    void onRequestStarted();

    void onRequestComplete(WritingResponse response);

    void onRequestError(String errorMessage);
}
