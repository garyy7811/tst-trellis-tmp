package tmp.trellis;

import org.apache.logging.log4j.util.Strings;
import tmp.trellis.externalserivce.AddressValidator;
import tmp.trellis.externalserivce.EmailValidator;
import tmp.trellis.externalserivce.LicenseValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class PolicyFieldsValidator{

    public PolicyFieldsValidator( LicenseValidator licenseValidator, EmailValidator emailValidator, AddressValidator addressValidator ){
        this.licenseValidator = licenseValidator;
        this.emailValidator = emailValidator;
        this.addressValidator = addressValidator;
    }

    public CompletableFuture<Void> process( List<InputPolicy> policies ){
        return CompletableFuture.allOf( policies.stream().map( p -> {
            return process( p );
        } ).collect( Collectors.toList() ).toArray( CompletableFuture[]::new ) );
    }

    CompletableFuture<Void> process( InputPolicy policy ){
        for( InputPolicy.Operator operator: policy.operators ){
            String birthErr = validateBirdthday( operator.birthdayRange );
            if( ! Strings.isBlank( birthErr ) ){
                operator.errorMsgLst.add( birthErr );
                policy.errorCount++;
            }
        }

        if( Strings.isBlank( policy.policyHolder.name.firstName ) ){
            policy.policyHolder.name.errorMsgLst.add( "First Name blank" );
            policy.errorCount++;
        }

        if( Strings.isBlank( policy.policyHolder.name.lastName ) ){
            policy.policyHolder.name.errorMsgLst.add( "Last Name blank" );
            policy.errorCount++;
        }

        List<CompletableFuture> futureLst = new ArrayList<>();

        List<InputPolicy.Operator> prmLs = policy.operators.stream().filter( o -> o.isPrimary ).collect( Collectors.toList() );
        if( prmLs.size() == 1 ){
            InputPolicy.Operator primeOperator = prmLs.get( 0 );

            if( ! Objects.equals( primeOperator.name, policy.policyHolder.name ) ){
                policy.errorMsgLst.add( "Policy holder name inconsistent with primary operator name" );
                policy.errorCount++;
            }
            if( ! Strings.isBlank( primeOperator.driversLicenseNumber ) ){
                futureLst.add(
                        validateLicense( primeOperator.driversLicenseNumber ).thenAccept( licenseErr -> {
                            primeOperator.errorMsgLst.add( licenseErr );
                            policy.errorCount++;
                        } )
                );
            }
            else{
                primeOperator.errorMsgLst.add( "Driver license blank" );
                policy.errorCount++;
            }
        }
        else if( prmLs.size() == 0 ){
            policy.policyHolder.errorMsgLst.add( "Primary holder not found" );
            policy.errorCount++;
        }
        else{
            policy.policyHolder.errorMsgLst.add( "More than one Primary holder found" );
            policy.errorCount++;
        }

        if( ! Strings.isBlank( policy.policyHolder.email ) ){
            futureLst.add( validateEmail( policy.policyHolder.email ).thenAccept( emailErr -> {
                if( ! Strings.isBlank( emailErr ) ){
                    policy.policyHolder.name.errorMsgLst.add( emailErr );
                    policy.errorCount++;
                }
            } ) );
        }
        else{
            policy.policyHolder.errorMsgLst.add( "Email is blank" );
            policy.errorCount++;
        }


        futureLst.add( validateAddress( policy.policyHolder.address ).thenAccept( addressErr -> {
            if( ! Strings.isBlank( addressErr ) ){
                policy.policyHolder.address.errorMsgLst.add( addressErr );
                policy.errorCount++;
            }
        } ) );

        return CompletableFuture.allOf( futureLst.toArray( CompletableFuture[]::new ) );
    }

    public static SimpleDateFormat BIRTH_DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );

    protected String validateBirdthday( InputPolicy.Operator.BirthdayRange birthdayRange ){
        if( Strings.isBlank( birthdayRange.start ) || Strings.isBlank( birthdayRange.end ) ){
            return "Birthday is blank";
        }

        try{
            Date start = BIRTH_DATE_FORMAT.parse( birthdayRange.start );
            Date end = BIRTH_DATE_FORMAT.parse( birthdayRange.end );
            if( end.compareTo( start ) < 0 ){
                return "Birthday range end is no later than start";
            }
        }
        catch( ParseException e ){
            return "Unknown Birthday format not following " + BIRTH_DATE_FORMAT.toPattern();
        }
        return null;
    }

    private LicenseValidator licenseValidator;

    protected CompletableFuture<String> validateLicense( String driversLicenseNumber ){
        //todo: convert driver license number object to 3rd party validator's format
        return licenseValidator.validate( driversLicenseNumber );
    }

    private EmailValidator emailValidator;

    /**
     * @param email
     * @return error messages if validated failed.
     */
    protected CompletableFuture<String> validateEmail( String email ){
        //todo: convert email object to 3rd party validator's format
        return emailValidator.validate( email.toString() );
    }


    private AddressValidator addressValidator;

    /**
     * @param address
     * @return error messages if validated failed.
     */
//    @Cacheable( evict= 6 month)
    protected CompletableFuture<String> validateAddress( InputPolicy.Holder.Address address ){
        //todo: convert address object to 3rd party validator's format
        return addressValidator.validate( address.toString() );
    }


}
