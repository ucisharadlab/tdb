
package edu.uci.ics.perpetual.statement.select;

import java.util.List;

/**
 * A database set operation. This operation consists of a list of plainSelects connected by set
 * operations (UNION,INTERSECT,MINUS,EXCEPT). All these operations have the same priority.
 *
 * @author tw
 */
public class SetOperationList implements SelectBody {

    private List<SelectBody> selects;
    private List<Boolean> brackets;
    private List<SetOperation> operations;
    private List<OrderByElement> orderByElements;
    private Limit limit;
    private Offset offset;
    private Fetch fetch;
    private int within;
    private int epoch;

    @Override
    public void accept(SelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public List<SelectBody> getSelects() {
        return selects;
    }

    public List<SetOperation> getOperations() {
        return operations;
    }

    public List<Boolean> getBrackets() {
        return brackets;
    }

    public void setBrackets(List<Boolean> brackets) {
        this.brackets = brackets;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setBracketsOpsAndSelects(List<Boolean> brackets, List<SelectBody> select, List<SetOperation> ops) {
        selects = select;
        operations = ops;
        this.brackets = brackets;

        if (select.size() - 1 != ops.size() || select.size() != brackets.size()) {
            throw new IllegalArgumentException("list sizes are not valid");
        }
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public Fetch getFetch() {
        return fetch;
    }

    public void setFetch(Fetch fetch) {
        this.fetch = fetch;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < selects.size(); i++) {
            if (i != 0) {
                buffer.append(" ").append(operations.get(i - 1).toString()).append(" ");
            }
            if (brackets == null || brackets.get(i)) {
                buffer.append("(").append(selects.get(i).toString()).append(")");
            } else {
                buffer.append(selects.get(i).toString());
            }
        }

        if (orderByElements != null) {
            buffer.append(PlainSelect.orderByToString(orderByElements));
        }
        if (limit != null) {
            buffer.append(limit.toString());
        }
        if (offset != null) {
            buffer.append(offset.toString());
        }
        if (fetch != null) {
            buffer.append(fetch.toString());
        }
        return buffer.toString();
    }

    public int getWithin() {
        return within;
    }

    public void setWithin(int within) {
        this.within = within;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    /**
     * list of set operations.
     */
    public enum SetOperationType {

        INTERSECT,
        EXCEPT,
        MINUS,
        UNION
    }
}
