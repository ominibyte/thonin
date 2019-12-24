package dev.thonin.messaging;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MessageHandler {
    void onResponse(MessageContext messageContext);
    void onFailure();
}
