package tmp.trellis.externalserivce;

import java.util.concurrent.CompletableFuture;

public interface AddressValidator{

    CompletableFuture<String> validate( String address );
}
