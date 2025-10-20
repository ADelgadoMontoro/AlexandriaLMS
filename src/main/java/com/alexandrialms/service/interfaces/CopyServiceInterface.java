package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.Copy;
import com.alexandrialms.exception.ValidationException;
import java.util.List;
import java.util.Optional;

public interface CopyServiceInterface {

    // CRUD OPERATIONS
    Copy createCopy(Copy copy) throws ValidationException;
    Optional<Copy> getCopyById(int copyId);
    List<Copy> getAllCopies();
    Optional<Copy> updateCopy(Copy copy) throws ValidationException;
    boolean deleteCopy(int copyId) throws ValidationException;

    // COPY MANAGEMENT BY BOOK
    List<Copy> getCopiesByBook(int bookId) throws ValidationException;
    List<Copy> getAvailableCopiesByBook(int bookId) throws ValidationException;
    List<Copy> getUnavailableCopiesByBook(int bookId) throws ValidationException;
    
    // AVAILABILITY AND STATUS
    List<Copy> getAvailableCopies();
    List<Copy> getUnavailableCopies();
    boolean isCopyAvailable(int copyId) throws ValidationException;
    boolean setCopyStatus(int copyId, String status) throws ValidationException;

    // STATISTICS AND COUNTS
    int getCopiesCountByBook(int bookId) throws ValidationException;
    int getAvailableCopiesCountByBook(int bookId) throws ValidationException;
    int getUnavailableCopiesCountByBook(int bookId) throws ValidationException;
    int getTotalCopiesCount();

    // LOAN-RELATED OPERATIONS
    List<Copy> getCopiesWithActiveLoans();
    List<Copy> getCopiesByLoanStatus(String loanStatus) throws ValidationException;

    // SEARCH OPERATIONS
    List<Copy> searchCopiesByInventoryNumber(String inventoryNumber) throws ValidationException;
    List<Copy> getCopiesByAcquisitionYear(int year) throws ValidationException;
    List<Copy> getCopiesByAcquisitionYearRange(int startYear, int endYear) throws ValidationException;

    // VALIDATION OPERATIONS
    boolean copyExists(int copyId);
    boolean copyExistsByInventoryNumber(String inventoryNumber);

    // MAINTENANCE OPERATIONS
    int deleteCopiesByBook(int bookId) throws ValidationException;
    boolean updateCopiesStatusByBook(int bookId, String newStatus) throws ValidationException;
    List<Copy> getOrphanedCopies();
    int deleteOrphanedCopies() throws ValidationException;

    // PAGINATION OPERATIONS
    List<Copy> getCopiesByBookPaginated(int bookId, int limit, int offset) throws ValidationException;
    List<Copy> getAvailableCopiesPaginated(int limit, int offset) throws ValidationException;
}