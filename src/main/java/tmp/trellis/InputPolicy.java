package tmp.trellis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputPolicy{

    public String issuer;
    public String issueDate;
    public String renewalDate;
    public String policyTermMonths;
    public String premiumCents;
    public Holder policyHolder;
    public List<Operator> operators;

    public List<String> errorMsgLst = new ArrayList<>();

    public int errorCount;

    public static class Holder{
        public Name name;
        public String phoneNumber;
        public String email;
        public Address address;

        public static class Address{
            public String number;
            public String street;
            public String type;
            public String sec_unit_type;
            public String sec_unit_num;
            public String suffix;
            public String city;
            public String state;
            public String zip;
            public String plus4;
            public List<String> errorMsgLst = new ArrayList<>();


            @Override
            public boolean equals( Object o ){
                if( this == o ) return true;
                if( o == null || getClass() != o.getClass() ) return false;
                Address address = ( Address )o;
                return Objects.equals( number, address.number ) && Objects.equals( street, address.street ) && Objects.equals( type, address.type ) && Objects.equals( sec_unit_type, address.sec_unit_type ) && Objects.equals( sec_unit_num, address.sec_unit_num ) && Objects.equals( suffix, address.suffix ) && Objects.equals( city, address.city ) && Objects.equals( state, address.state ) && Objects.equals( zip, address.zip ) && Objects.equals( plus4, address.plus4 );
            }

            @Override
            public int hashCode(){
                return Objects.hash( number, street, type, sec_unit_type, sec_unit_num, suffix, city, state, zip, plus4 );
            }

        }

        public List<String> errorMsgLst = new ArrayList<>();

    }

    public static class Operator{

        public boolean isPrimary;

        public Name name;
        public BirthdayRange birthdayRange;

        public String gender;

        public String driversLicenseStatus;
        public String driversLicenseState;
        public String driversLicenseNumber;
        public String relationship;

        public List<String> errorMsgLst = new ArrayList<>();

        public static class BirthdayRange{
            public String start;
            public String end;

            @Override
            public boolean equals( Object o ){
                if( this == o ) return true;
                if( o == null || getClass() != o.getClass() ) return false;
                BirthdayRange that = ( BirthdayRange )o;
                return Objects.equals( start, that.start ) && Objects.equals( end, that.end );
            }

            @Override
            public int hashCode(){
                return Objects.hash( start, end );
            }
        }

        @Override
        public boolean equals( Object o ){
            if( this == o ) return true;
            if( o == null || getClass() != o.getClass() ) return false;
            Operator operator = ( Operator )o;
            return isPrimary == operator.isPrimary && Objects.equals( name, operator.name ) && Objects.equals( birthdayRange, operator.birthdayRange ) && Objects.equals( gender, operator.gender ) && Objects.equals( driversLicenseStatus, operator.driversLicenseStatus ) && Objects.equals( driversLicenseState, operator.driversLicenseState ) && Objects.equals( driversLicenseNumber, operator.driversLicenseNumber ) && Objects.equals( relationship, operator.relationship );
        }

        @Override
        public int hashCode(){
            return Objects.hash( isPrimary, name, birthdayRange, gender, driversLicenseStatus, driversLicenseState, driversLicenseNumber, relationship );
        }
    }

    public static class Name{
        public String firstName;
        public String middleName;
        public String lastName;
        public List<String> errorMsgLst = new ArrayList<>();


        @Override
        public boolean equals( Object o ){
            if( this == o ) return true;
            if( o == null || getClass() != o.getClass() ) return false;
            Name name = ( Name )o;
            return Objects.equals( firstName, name.firstName ) && Objects.equals( middleName, name.middleName ) && Objects.equals( lastName, name.lastName );
        }

        @Override
        public int hashCode(){
            return Objects.hash( firstName, middleName, lastName );
        }
    }

    @Override
    public boolean equals( Object o ){
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;
        InputPolicy that = ( InputPolicy )o;
        return Objects.equals( issuer, that.issuer ) && Objects.equals( issueDate, that.issueDate ) && Objects.equals( renewalDate, that.renewalDate ) && Objects.equals( policyTermMonths, that.policyTermMonths ) && Objects.equals( premiumCents, that.premiumCents ) && Objects.equals( policyHolder, that.policyHolder ) && Objects.equals( operators, that.operators );
    }

    @Override
    public int hashCode(){
        return Objects.hash( issuer, issueDate, renewalDate, policyTermMonths, premiumCents, policyHolder, operators );
    }
}

