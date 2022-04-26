package dramaholic.actor;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;

public class ActorSequenceGen extends SequenceStyleGenerator{

    /**
     * Custom id generation. If id is set on the
     * com.curecomp.common.hibernate.api.Entity instance then use the set one,
     * if id is 'null' or '0' then generate one.
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        if (obj instanceof Actor) {
            Actor entity = (Actor) obj;
            if (entity.getId() == null) {
                return super.generate(session, obj);
            }
            return entity.getId();
        }
        return super.generate(session, obj);
    }
}