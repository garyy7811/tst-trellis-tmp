package tmp.trellis.externalserivce;

import java.util.concurrent.CompletableFuture;

public interface EmailValidator{

    CompletableFuture<String> validate( String email );
}
