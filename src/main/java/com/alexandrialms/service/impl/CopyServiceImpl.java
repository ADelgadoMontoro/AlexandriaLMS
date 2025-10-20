package com.alexandrialms.service.impl;

import java.util.List;
import java.util.Optional;

import com.alexandrialms.dao.impl.BookDAO;
import com.alexandrialms.dao.impl.CopyDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.Copy;
import com.alexandrialms.service.interfaces.CopyServiceInterface;
import com.alexandrialms.util.ValidationHelper;

public class CopyServiceImpl implements CopyServiceInterface {
    CopyDAO copyDAO = new CopyDAO();
    BookDAO bookDAO = new BookDAO();

    @Override
    public Copy createCopy(Copy copy) throws ValidationException {
        ValidationHelper.validateCopyForInsert(copy, copyDAO, bookDAO);
        try {
            if (copyDAO.insert(copy)) {
                Copy savedCopy = copyDAO.findByInternalCode(copy.getInternal_code());
                return savedCopy;
            } 
        } catch (Exception e) {
            throw new ValidationException("Failed to create copy: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<Copy> getCopyById(int copyId) {
        if (!ValidationHelper.isValidCopyID(copyId, copyDAO)) {
            return Optional.empty();
        }
        Optional<Copy> copy = Optional.ofNullable(copyDAO.findById(copyId));
        return copy;
    }

    @Override
    public List<Copy> getAllCopies() {
        List<Copy> copies = copyDAO.findAll();
        return copies;
    }

    @Override
    public Optional<Copy> updateCopy(Copy copy) throws ValidationException {
        ValidationHelper.validateCopyForUpdate(copy, copyDAO, bookDAO);
        
        try {
            if (copyDAO.update(copy)) {
                Copy updatedCopy = copyDAO.findById(copy.getCopyID());
                return Optional.ofNullable(updatedCopy);
            } 
        } catch (Exception e) {
            throw new ValidationException("Failed to update copy: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCopy(int copyId) throws ValidationException {
        if (!ValidationHelper.isValidCopyID(copyId, copyDAO)) {
            throw new ValidationException("Invalid copy ID: " + copyId);
        }
        
        try {
            return copyDAO.delete(copyId);
        } catch (Exception e) {
            throw new ValidationException("Failed to delete copy: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Copy> getCopiesByBook(int bookId) throws ValidationException {
        if (bookId <= 0) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        List<Copy> copies = copyDAO.findByBookId(bookId);
        return copies;
    }

    @Override
    public List<Copy> getAvailableCopiesByBook(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        List<Copy> copies = copyDAO.findAvailableByBookId(bookId);
        return copies;
    }

    @Override
    public List<Copy> getUnavailableCopiesByBook(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        List<Copy> copies = copyDAO.findUnavailableByBookId(bookId);
        return copies;
    }

    @Override
    public List<Copy> getAvailableCopies() {
        List<Copy> copies = copyDAO.findAvailableCopies();
        return copies;
    }

    @Override
    public List<Copy> getUnavailableCopies() {
        List<Copy> copies = copyDAO.findUnavailableCopies();
        return copies;
    }

    @Override
    public boolean isCopyAvailable(int copyId) throws ValidationException {
        if (!ValidationHelper.isValidCopyID(copyId, copyDAO)) {
            throw new ValidationException("Invalid copy ID: " + copyId);
        }
        Copy copy = copyDAO.findById(copyId);
        return copy != null && copy.getStatus().name().equalsIgnoreCase("AVAILABLE");
    }

@Override
public boolean setCopyStatus(int copyId, String status) throws ValidationException {
    if (!ValidationHelper.isValidCopyID(copyId, copyDAO)) {
        throw new ValidationException("Invalid copy ID: " + copyId);
    }
    
    ValidationHelper.validateCopyStatus(status);
    
    try {
        return copyDAO.setCopyStatus(copyId, status.toUpperCase());
    } catch (Exception e) {
        throw new ValidationException("Failed to update copy status: " + e.getMessage(), e);
    }
}

    @Override
    public int getCopiesCountByBook(int bookId) throws ValidationException {
            if (bookId <= 0) {
        throw new ValidationException("Book ID must be positive: " + bookId);
    }
        return copyDAO.countCopiesByBook(bookId);
    }

    @Override
    public int getAvailableCopiesCountByBook(int bookId) throws ValidationException {
            if (bookId <= 0) {
        throw new ValidationException("Book ID must be positive: " + bookId);
    }
        return copyDAO.countAvailableCopiesByBook(bookId);
    }

    @Override
    public int getUnavailableCopiesCountByBook(int bookId) throws ValidationException {
            if (bookId <= 0) {
        throw new ValidationException("Book ID must be positive: " + bookId);
    }
        return copyDAO.countUnavailableCopiesByBook(bookId);
    }

    @Override
    public int getTotalCopiesCount() {
        return copyDAO.countAllCopies();
    }

    @Override
    public List<Copy> getCopiesWithActiveLoans() {
        List<Copy> copies = copyDAO.findCopiesWithActiveLoans();
        return copies;
    }

    @Override
    public List<Copy> getCopiesByLoanStatus(String loanStatus) throws ValidationException {
        if (!ValidationHelper.isValidString(loanStatus, 1)) {
            throw new ValidationException("loanStatus", "INVALID_LOAN_STATUS", "Loan status is required");
        }
        List<Copy> copies = copyDAO.findCopiesByLoanStatus(loanStatus.toUpperCase());
        return copies;
    }

    @Override
    public List<Copy> searchCopiesByInventoryNumber(String inventoryNumber) throws ValidationException {
        if (!ValidationHelper.isValidString(inventoryNumber, 1)) {
            throw new ValidationException("Invalid inventory number: " + inventoryNumber);
        }
        List<Copy> copies = copyDAO.searchCopiesByInternalCode(inventoryNumber);
        return copies;
    }

    @Override
    public List<Copy> getCopiesByAcquisitionYear(int year) throws ValidationException {
        int currentYear = java.time.Year.now().getValue();
        if (year < 1900 || year > currentYear) {
            throw new ValidationException("Invalid acquisition year: " + year);
        }
        List<Copy> copies = copyDAO.findCopiesByAcquisitionYear(year);
        return copies;
    }

    @Override
    public List<Copy> getCopiesByAcquisitionYearRange(int startYear, int endYear) throws ValidationException {
        int currentYear = java.time.Year.now().getValue();
        if (startYear < 1900 || endYear > currentYear || startYear > endYear) {
            throw new ValidationException("Invalid acquisition year range: " + startYear + " - " + endYear);
        }
        List<Copy> copies = copyDAO.findCopiesByAcquisitionYearRange(startYear, endYear);
        return copies;
    }

    @Override
    public boolean copyExists(int copyId) {
            if (copyId <= 0) {
        throw new ValidationException("Book ID must be positive: " + copyId);
    }
        return copyDAO.findById(copyId) != null;
    }

    @Override
    public boolean copyExistsByInventoryNumber(String inventoryNumber) {
        if (!ValidationHelper.isValidString(inventoryNumber, 1)) {
            throw new ValidationException("copyId", "INVALID_COPY_ID", "Invalid inventory number: " + inventoryNumber);
        }
        return copyDAO.findByInternalCode(inventoryNumber) != null;
    }

    @Override
    public int deleteCopiesByBook(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        try {
            return copyDAO.deleteCopiesByBook(bookId);
        } catch (Exception e) {
            throw new ValidationException("Failed to delete copies by book: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateCopiesStatusByBook(int bookId, String newStatus) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        
        ValidationHelper.validateCopyStatus(newStatus);
        
        try {
            return copyDAO.updateCopiesStatusByBook(bookId, newStatus.toUpperCase()) != 0;
        } catch (Exception e) {
            throw new ValidationException("Failed to update copies status by book: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Copy> getOrphanedCopies() {
        List<Copy> copies = copyDAO.findOrphanedCopies();
        return copies;
    }

    @Override
    public int deleteOrphanedCopies() throws ValidationException {
        List<Copy> orphanedCopies = copyDAO.findOrphanedCopies();
        for (Copy copy : orphanedCopies) {
            copyDAO.delete(copy.getCopyID());
        }
        return orphanedCopies.size();
    }

    @Override
    public List<Copy> getCopiesByBookPaginated(int bookId, int limit, int offset) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        List<Copy> copies = copyDAO.findByBookIdPaginated(bookId, limit, offset);
        return copies;
    }

    @Override
    public List<Copy> getAvailableCopiesPaginated(int limit, int offset) throws ValidationException {
        List<Copy> copies = copyDAO.findAvailableCopiesPaginated(limit, offset);
        return copies;
    }
    
}
