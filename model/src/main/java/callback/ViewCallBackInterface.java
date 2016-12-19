package callback;

/**
 * Created by Jon on 12/16/2016.
 */

public interface ViewCallBackInterface {
    public enum CallBackCommand {
        COLOR_UPDATE
    }

    public void update(CallBackCommand command);
}
