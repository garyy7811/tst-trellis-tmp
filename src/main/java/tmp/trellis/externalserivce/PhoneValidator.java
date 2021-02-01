package tmp.trellis.externalserivce;

import java.util.concurrent.CompletableFuture;

public interface PhoneValidator{

    CompletableFuture<String> validate( String phoneNumber );
}
