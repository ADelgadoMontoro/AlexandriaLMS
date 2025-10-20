package com.alexandrialms.dao.interfaces;

import java.util.List;

import com.alexandrialms.model.Copy;

public interface CopyDAOInterface {
    // SEARCHES BY BOOK
    List<Copy> findByBookId(int bookId);
    List<Copy> findAvailableByBookId(int bookId);
    List<Copy> findUnavailableByBookId(int bookId);
    
    // STATUS-BASED SEARCHES
    List<Copy> findAvailableCopies();
    List<Copy> findUnavailableCopies();
    boolean isCopyAvailable(int copyId);
    boolean setCopyStatus(int copyId, String status);
    
    // STATISTICAL QUERIES
    int countCopiesByBook(int bookId);
    int countAvailableCopiesByBook(int bookId);
    int countUnavailableCopiesByBook(int bookId);
    
    // LOANS MANAGEMENT
    List<Copy> findCopiesWithActiveLoans();
    List<Copy> findCopiesByLoanStatus(String loanStatus);
    
    // ADVANCED SEARCHES
    List<Copy> searchCopiesByInternalCode(String inventoryNumber);
    List<Copy> findCopiesByAcquisitionYear(int year);
    List<Copy> findCopiesByAcquisitionYearRange(int startYear, int endYear);
    Copy findByInternalCode(String internalCode);
    
    // VALIDATION METHODS
    boolean existsByInternalCode(String inventoryNumber);
    int countAllCopies();
    
    // MAINTENANCE OPERATIONS
    int deleteCopiesByBook(int bookId);
    int updateCopiesStatusByBook(int bookId, String newStatus);
    List<Copy> findOrphanedCopies(); // Copias sin libro asociado
    
    // PAGINATION SUPPORT
    List<Copy> findByBookIdPaginated(int bookId, int limit, int offset);
    List<Copy> findAvailableCopiesPaginated(int limit, int offset);
}
