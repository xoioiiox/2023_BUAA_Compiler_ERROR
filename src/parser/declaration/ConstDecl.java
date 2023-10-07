package parser.declaration;

import java.util.ArrayList;

public class ConstDecl extends Decl {
    Btype btype;
    ArrayList<ConstDef> constDefs;

    public ConstDecl(Btype btype, ArrayList<ConstDef> constDefs) {
        this.btype = btype;
        this.constDefs = constDefs;
    }


}
