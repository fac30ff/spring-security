package hello.db;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class DBRoles {

    public static String USER="USER";
    public static String ADMIN="ADMIN";

    public static String[] get() {
        return new String[]{USER};
    }

    public static String[] getA() {
        return new String[]{ADMIN};
    }

    public static String[] getUA() {
        return new String[]{USER, ADMIN};
    }

    public static Collection<GrantedAuthority> auth() {
        return new ArrayList<GrantedAuthority>(1) {{
            add(new SimpleGrantedAuthority(DBRoles.USER));
        }};
    }

    public static Collection<GrantedAuthority> allRoles() {
        return new ArrayList<GrantedAuthority>(2) {{
            add(new SimpleGrantedAuthority(DBRoles.USER));
            add(new SimpleGrantedAuthority(DBRoles.ADMIN));
        }};
    }

}
