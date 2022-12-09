package Auberginn;

/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple -- livre inexistant
 */
public final class AuberginnException extends Exception
{
    private static final long serialVersionUID = 1L;

    public AuberginnException(String message)
    {
        super(message);
    }
}