package dto;

/**
 * Request DTO for company representative registration.
 * Contains all necessary information for a new representative to register.
 * 
 * @author SC2002 SCED Group-6
 * @version 1.0
 * @since 2025-10-10
 */
public class RegisterRepresentativeRequest {
    private String representativeId;
    private String name;
    private String email;
    private String password;
    private String companyName;
    private String industry;
    private String position;

    // Default constructor
    public RegisterRepresentativeRequest() {
    }

    // Parameterized constructor
    public RegisterRepresentativeRequest(String representativeId, String name, String email, 
                                        String password, String companyName, String industry, String position) {
        this.representativeId = representativeId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.industry = industry;
        this.position = position;
    }

    // Getters and Setters
    public String getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(String representativeId) {
        this.representativeId = representativeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "RegisterRepresentativeRequest{" +
                "representativeId='" + representativeId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
