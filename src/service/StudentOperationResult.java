package service;

public class StudentOperationResult {
    private final OperationStatus status;
    private final ValidationResult validationResult;
    public StudentOperationResult(OperationStatus status , ValidationResult validationResult){
        this.status = status;
        this.validationResult = validationResult;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
