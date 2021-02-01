package tmp.trellis.externalserivce;

import java.util.concurrent.CompletableFuture;

public interface LicenseValidator{

    CompletableFuture<String> validate( String license );
}
