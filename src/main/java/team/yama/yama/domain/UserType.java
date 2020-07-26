package team.yama.yama.domain;

public enum UserType {
    //Employees are the workers under property management company
    //Managers are workers under third party company
    //Workers are employees under managers
    Admin(0), Employee(1), Manager(2), Worker(3), Tenant(4);

    private final Integer code;

    UserType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
