package parser.declaration;

import java.util.ArrayList;

public class VarDecl extends Decl {
    Btype btype;
    ArrayList<VarDef> varDefs;

    public VarDecl(Btype btype, ArrayList<VarDef> varDefs) {
        this.btype = btype;
        this.varDefs = varDefs;
    }
}
