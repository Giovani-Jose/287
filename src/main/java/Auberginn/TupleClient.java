package Auberginn;

public class TupleClient
{
    //region Fields
    private int idClient;
    private String nom;
    private String premom;
    private int age;
    private String utilisateurName;
    private String motDePasse;
    private int acces;

    //endregion

    //region Construtors
    public TupleClient() {
    }

    public TupleClient(String utilisateur, String motDePasse, int acces, String nom, String premom, int age) {
        this.setUtilisateurName(utilisateur);
        this.setMotDePasse(motDePasse);
        this.setNiveauAcces(acces);

        this.idClient = idClient;
        this.nom = nom;
        this.premom = premom;
        this.age = age;
    }
    //endregion

    //region Getters/Setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPremom() {
        return premom;
    }

    public void setPremom(String premom) {
        this.premom = premom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void printInfo()
    {
        System.out.println("\n" + "Client : " + idClient);
        System.out.println("Nom : " + nom);
        System.out.println("Prenom : " + premom);
        System.out.println("Age : " + age);
    }
    public void setUtilisateurName(String utilisateurName)
    {
        this.utilisateurName = utilisateurName;
    }
    public void setNiveauAcces(int niveau)
    {
        this.acces = niveau;
    }
    public int getNiveauAcces()
    {
        return acces;
    }

    public String getUtilisateurName()
    {
        return utilisateurName;
    }

    public void setMotDePasse(String motDePasse)
    {
        this.motDePasse = motDePasse;
    }

    public String getMotDePasse()
    {
        return motDePasse;
    }
    //endregion
}
