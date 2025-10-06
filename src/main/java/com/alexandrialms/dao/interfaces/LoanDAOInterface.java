package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Loan;
import java.sql.SQLException;
import java.util.List;

public interface LoanDAOInterface extends GenericDAO<Loan, Integer> {
    List<Loan> findActiveLoans() throws SQLException;
    List<Loan> findByUser(int userID) throws SQLException;
}
