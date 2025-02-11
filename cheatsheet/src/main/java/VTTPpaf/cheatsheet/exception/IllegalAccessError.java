package VTTPpaf.cheatsheet.exception;

public class IllegalAccessError extends RuntimeException {
    public IllegalAccessError(){
        super();
    }

    public IllegalAccessError(String message){
        super(message);
    }

    public IllegalAccessError(String message, Throwable cause){
        super(message, cause);
    }

}
