package com.alexandrialms.dao;

import com.alexandrialms.dao.interfaces.LoanDAOInterface;
import com.alexandrialms.model.Loan;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO implements LoanDAOInterface {
    @Override
    public boolean insert(Loan loan) {
        String sql = "INSERT INTO Loans (copy_id, user_id, loan_date, return_date, returned) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, loan.getCopyID());
            pstm.setInt(2, loan.getUserID());
            pstm.setDate(3, Date.valueOf(loan.getLoanDate()));

            if (loan.getReturnDate() != null) {
                pstm.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                pstm.setNull(4, Types.DATE);
            }

            pstm.setBoolean(5, loan.isReturned());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Loan loan) {
        String sql = "UPDATE Loans SET copy_id = ?, user_id = ?, loan_date = ?, return_date = ?, returned = ? WHERE loan_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, loan.getCopyID());
            pstm.setInt(2, loan.getUserID());
            pstm.setDate(3, Date.valueOf(loan.getLoanDate()));

            if (loan.getReturnDate() != null) {
                pstm.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                pstm.setNull(4, Types.DATE);
            }

            pstm.setBoolean(5, loan.isReturned());
            pstm.setInt(6, loan.getLoanID());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer loanID) {
        String sql = "DELETE FROM Loans WHERE loan_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, loanID);

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Loan findById(Integer loanID) {
        String sql = "SELECT * FROM Loans WHERE loan_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, loanID);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    private Loan mapResultSet(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanID(rs.getInt("loan_id"));
        loan.setCopyID(rs.getInt("copy_id"));
        loan.setUserID(rs.getInt("user_id"));

        Date loanDate = rs.getDate("loan_date");
        if (loanDate != null) {
            loan.setLoanDate(loanDate.toLocalDate());
        }

        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }

        loan.setReturned(rs.getBoolean("returned"));

        return loan;
    }

    @Override
    public List<Loan> findActiveLoans() {
        List<Loan> activeLoans = new ArrayList<>();
        String sql = "SELECT loan_id, copy_id, user_id, loan_date, return_date, returned FROM loans WHERE returned = false;";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                activeLoans.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activeLoans;
    }

    @Override
    public List<Loan> findByUser(int userID) {
        List<Loan> userLoans = new ArrayList<>();
        String sql = "SELECT loan_id, copy_id, user_id, loan_date, return_date, returned FROM loans WHERE user_id = ?;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userLoans.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userLoans;
    }
}