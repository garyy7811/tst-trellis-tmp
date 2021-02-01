package tmp.trellis;

import org.apache.logging.log4j.util.Strings;
import tmp.trellis.externalserivce.AddressValidator;
import tmp.trellis.externalserivce.EmailValidator;
import tmp.trellis.externalserivce.LicenseValidator;
import tmp.trellis.externalserivce.PhoneValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RanchersPolicyFieldsValidatorImpl extends PolicyFieldsValidator{

    public RanchersPolicyFieldsValidatorImpl( LicenseValidator licenseValidator, EmailValidator emailValidator, AddressValidator addressValidator, PhoneValidator phoneValidator ){
        super( licenseValidator, emailValidator, addressValidator );
        this.phoneValidator = phoneValidator;
    }

    private PhoneValidator phoneValidator;

    @Override
    CompletableFuture<Void> process( InputPolicy policy ){
        CompletableFuture<Void> rt = super.process( policy );

        List<CompletableFuture<Void>> ls = new ArrayList<>();
        if(! Strings.isBlank(  policy.policyHolder.phoneNumber )){
            ls.add( validatePhone( policy.policyHolder.phoneNumber ).thenAccept( phoneErr -> {
                if( ! Strings.isBlank( phoneErr ) ){
                    policy.policyHolder.errorMsgLst.add( phoneErr );
                    policy.errorCount++;
                }
            } ) );
        }
        else{
            policy.policyHolder.errorMsgLst.add( "PhoneNumber blank" );
            policy.errorCount++;
        }

        ls.addAll( policy.operators.stream().map( o -> {
            if( Strings.isBlank( o.driversLicenseNumber ) ){
                o.errorMsgLst.add( "Driver license blank" );
                policy.errorCount++;
                return null;
            }
            return validateLicense( o.driversLicenseNumber ).thenAccept( licenseErr -> {
                if( ! Strings.isBlank( licenseErr ) ){
                    o.errorMsgLst.add( licenseErr );
                    policy.errorCount++;
                }
            } );
        } ).filter( Objects::nonNull ).collect( Collectors.toList() ) );

        ls.add( rt );
        return CompletableFuture.allOf( ls.toArray( CompletableFuture[]::new ) );
    }

    /**
     * @param phoneNumber
     * @return error messages if validated failed.
     */
//    @Cacheable( evict= 6 month)
    CompletableFuture<String> validatePhone( String phoneNumber ){
        //todo: convert phoneNumber object to 3rd party validator's format
        return phoneValidator.validate( phoneNumber.toString() );
    }
}
