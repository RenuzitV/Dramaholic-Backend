package dramaholic.movie;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;

public class MovieSequenceGen extends SequenceStyleGenerator{

    /**
     * Custom id generation. If id is set on the
     * com.curecomp.common.hibernate.api.Entity instance then use the set one,
     * if id is 'null' or '0' then generate one.
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        if (obj instanceof Movie) {
            Movie entity = (Movie) obj;
            if (entity.getDbID() == null) {
                return super.generate(session, obj);
            }
            return entity.getDbID();
        }
        return super.generate(session, obj);
    }
}