package fr.ordinalteam.ordinalteamweb.dto;

public class CategoryDTO {
    private String name;
    private String description;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                '}';
    }
}
