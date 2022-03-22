
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class EqualsTo extends ComparisonOperator {

    public EqualsTo() {
        super("=");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
