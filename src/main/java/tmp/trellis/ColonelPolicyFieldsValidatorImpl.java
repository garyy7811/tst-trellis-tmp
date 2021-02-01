package tmp.trellis;

import tmp.trellis.externalserivce.AddressValidator;
import tmp.trellis.externalserivce.EmailValidator;
import tmp.trellis.externalserivce.LicenseValidator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColonelPolicyFieldsValidatorImpl extends PolicyFieldsValidator{

    List<String> genderValues = List.of( "male", "female" );

    public ColonelPolicyFieldsValidatorImpl( LicenseValidator licenseValidator, EmailValidator emailValidator, AddressValidator addressValidator ){
        super( licenseValidator, emailValidator, addressValidator );
    }

    @Override
    CompletableFuture<Void> process( InputPolicy policy ){
        CompletableFuture<Void> rt = super.process( policy );
        policy.operators.forEach( o -> {
            if( ! genderValues.contains( o.gender ) ){
                o.errorMsgLst.add( "Missing gender" );
                policy.errorCount++;
            }
        } );
        return rt;
    }
}
