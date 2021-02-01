
package tmp.trellis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import tmp.trellis.externalserivce.AddressValidator;
import tmp.trellis.externalserivce.EmailValidator;
import tmp.trellis.externalserivce.LicenseValidator;
import tmp.trellis.externalserivce.PhoneValidator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App{
    public static void main( String[] args ) throws IOException{
        ObjectMapper objectMapper = new JsonMapper();

        TypeReference<List<InputPolicy>> type = new TypeReference<>(){
        };

        List<InputPolicy> policiesForColonel = objectMapper.readValue( App.class.getClassLoader().getResourceAsStream( "policies.json" ), type );
        new ColonelPolicyFieldsValidatorImpl( licenseValidator, emailValidator, addressValidator ).process( policiesForColonel ).join();

        List<InputPolicy> policiesForRanchers = objectMapper.readValue( App.class.getClassLoader().getResourceAsStream( "policies.json" ), type );


        new RanchersPolicyFieldsValidatorImpl( licenseValidator, emailValidator, addressValidator, phoneValidator ).process( policiesForRanchers ).join();
/*
        System.out.println( "Policies >> For >> Colonel" );
        System.out.println( objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( policiesForColonel ) );
        System.out.println( "Policies << For << Colonel" );*/

        System.out.println( "Policies >> For >> Ranchers" );
        System.out.println( objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( policiesForRanchers ) );
        System.out.println( "Policies << For << Ranchers" );
    }

    protected static Logger logger = LogManager.getLogger( App.class );

    static PhoneValidator phoneValidator = new PhoneValidator(){
        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";

        Pattern pattern = Pattern.compile( regex );

        @Override
        public CompletableFuture<String> validate( String phoneNumber ){
            Matcher matcher = pattern.matcher( phoneNumber );

            return CompletableFuture.supplyAsync( () -> {
                boolean formatted = matcher.matches();
                if( ! formatted ){
                    return "invalidate email in format";
                }
                //todo: validating phone really exist by ATT?
                return null;
            } );
        }
    };


    static LicenseValidator licenseValidator = license -> CompletableFuture.supplyAsync( () -> {
        // TODO: validate license by accessing MVC ???
        return license.length() < 5 ? "License number not long even" : null;
    } );

    static EmailValidator emailValidator = new EmailValidator(){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile( regex );

        @Override
        public CompletableFuture<String> validate( String email ){

            Matcher matcher = pattern.matcher( email );

            return CompletableFuture.supplyAsync( () -> {
                boolean formatted = matcher.matches();
                if( ! formatted ){
                    return "invalidate email in format";
                }
                //todo: validating email really by email servefr
                return null;
            } );
        }
    };
    static AddressValidator addressValidator = address -> CompletableFuture.supplyAsync( () -> {
        //todo: address really exist through USPS ??
        return Strings.isBlank( address ) ? "blank address" : null;
    } );

    //todo: cache validators IO

    //todo: define and handle exceptions

}
