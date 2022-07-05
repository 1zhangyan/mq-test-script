import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThreadStatistics {

    private Long millisTotal;

    private Long sendSuccessTotal;

    private Long sendFailTotal;

    private Long rateLimitValue;

}
