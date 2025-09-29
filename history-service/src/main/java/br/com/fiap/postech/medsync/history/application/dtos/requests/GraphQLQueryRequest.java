package br.com.fiap.postech.medsync.history.application.dtos.requests;

public class GraphQLQueryRequest {
    private String query;
    private String operationName;
    private Object variables;

    // Construtores
    public GraphQLQueryRequest() {}

    public GraphQLQueryRequest(String query) {
        this.query = query;
    }

    // Getters e Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Object getVariables() {
        return variables;
    }

    public void setVariables(Object variables) {
        this.variables = variables;
    }
}