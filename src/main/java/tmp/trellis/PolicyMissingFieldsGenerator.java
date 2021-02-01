package tmp.trellis;

import java.util.List;

public class PolicyMissingFieldsGenerator{


    public void process( List<InputPolicy> policies, PolicyFieldsValidator validator ){
        validator.process( policies );
    }

}
