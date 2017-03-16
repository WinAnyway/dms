package pl.com.bottega.dms.application.user.impl;

import pl.com.bottega.dms.application.user.CurrentUser;
import pl.com.bottega.dms.model.EmployeeId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StandardCurrentUser implements CurrentUser {

    private EmployeeId employeeId;

    @Override
    public void setEmployeeId(EmployeeId employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    @Override
    public Set<String> getRoles() {
        if (employeeId.getId() == 1)
            return new HashSet<>(Arrays.asList("STAFF", "QUALITY_STAFF", "STAFF_MANAGER"));
         else if (employeeId.getId() == 2)
            return new HashSet<>(Arrays.asList("STAFF", "QUALITY_STAFF"));
         else
            return new HashSet<>(Arrays.asList("STAFF"));
    }
}
