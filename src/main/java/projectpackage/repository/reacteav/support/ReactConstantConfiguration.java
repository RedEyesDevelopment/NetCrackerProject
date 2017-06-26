package projectpackage.repository.reacteav.support;

import org.springframework.stereotype.Component;

@Component
public class ReactConstantConfiguration {
    //OBJECTS TABLE NAME
    private String objectsTableName;
    //OBJECTS TYPE TABLE NAME
    private String objTypesTableName;
    //ATTRIBUTES TABLE NAME
    private String attributesTableName;
    //ATTRTYPES TABLE NAME
    private String attrTypesTableName;
    //OBJREFERENCE TABLE NAME
    private String objRefsTableName;
    //PERMANENT STRING FOR GENERATING ATTRIBUTES TABLE NAME
    private String attributesPermanentTableName;
    //STRING FOR ROOT OBJECT TABLE NAME
    private String rootTableName;
    //STRING FOR OBJTYPE OF ROOT ENTITY
    private String rootTypesTableName;
    //PERMANENT STRING FOR GENERATING ATTRTYPES TABLE NAME
    private String attrTypesPermanentTableName;
    //REFERENCE OBJECTS GENERATOR
    private String referenceTypePermanentTableName;
    //OBJECT_TYPE_ID
    private String otid;
    //OBJECT_TYPE_ID_REF
    private String otidref;
    //REFERENCE
    private String oref;
    //OBJECT_ID
    private String oid;
    //ATTR_ID
    private String aid;
    //CODE
    private String cd;
    //VALUE
    private String v;
    //DATE_VALUE
    private String dv;
    //DATE COLUMN NAME APPENDER
    private String dateAppender;
    //CONSTANT NAME FOR OBJECT_TYPE_ID VALUE
    private String entityOrderConstant;
    //CONSTANT NAME FOR OBJECT_TYPE_ID VALUE
    private String entityTypeIdConstant;
    //CONSTANT NAME FOR OBJECT_TYPE_ID VALUE
    private String entityIdConstant;
    //"\nAND"
    private String newLineAnd;
    //" OR "
    private String spacedOr;
    //".OBJECT_ID="
    private String paramObjectId;
    //".PARENT_ID="
    private String paramParentId;
    //"("
    private char lbracketChar;
    //")"
    private char rbracketChar;
    //NEW LINE CHAR;
    private char newLineChar;
    //POINT CHAR;
    private char pointChar;
    //" " "
    private char doubleBracket;
    //" ' "
    private char singleBracket;
    //space
    private char spaceChar;
    //SELECT FROM WHERE
    private String selectWord;
    private String fromWord;
    private String whereWord;
    //mapParameterEqualitySign;
    private String mapEqualitySign;
    private char equalitySign;


    public ReactConstantConfiguration() {
        objectsTableName = "OBJECTS";
        objTypesTableName = "OBJTYPE";
        attributesTableName = "ATTRIBUTES";
        attrTypesTableName = "ATTRTYPE";
        objRefsTableName = "OBJREFERENCE";
        attributesPermanentTableName = "ATTRS";
        rootTableName = "ROOTABLE";
        attrTypesPermanentTableName = "ATTYPES";
        referenceTypePermanentTableName = "REFOB";
        otid = "OBJECT_TYPE_ID";
        otidref = "OBJECT_TYPE_ID_REF";
        oref = "REFERENCE";
        oid = "OBJECT_ID";
        aid = "ATTR_ID";
        cd = "CODE";
        v = "VALUE";
        dv = "DATE_VALUE";
        dateAppender = "_date";
        entityTypeIdConstant = "targetTypeId";
        entityIdConstant = "targetId";
        entityOrderConstant = "enorder";
        rootTypesTableName = "ROOTYPES";
        newLineAnd = "\nAND ";
        spacedOr = " OR ";
        paramObjectId = ".OBJECT_ID=";
        paramParentId = ".PARENT_ID=";
        lbracketChar ='(';
        rbracketChar =')';
        newLineChar='\n';
        pointChar='.';
        doubleBracket='"';
        singleBracket='\'';
        spaceChar=' ';
        selectWord="SELECT";
        fromWord="FROM";
        whereWord="WHERE";
        mapEqualitySign = "=:";
        equalitySign = '=';
    }

    public String getObjectsTableName() {
        return objectsTableName;
    }

    public void setObjectsTableName(String objectsTableName) {
        this.objectsTableName = objectsTableName;
    }

    public String getObjTypesTableName() {
        return objTypesTableName;
    }

    public void setObjTypesTableName(String objTypesTableName) {
        this.objTypesTableName = objTypesTableName;
    }

    public String getAttributesTableName() {
        return attributesTableName;
    }

    public void setAttributesTableName(String attributesTableName) {
        this.attributesTableName = attributesTableName;
    }

    public String getAttrTypesTableName() {
        return attrTypesTableName;
    }

    public void setAttrTypesTableName(String attrTypesTableName) {
        this.attrTypesTableName = attrTypesTableName;
    }

    public String getObjRefsTableName() {
        return objRefsTableName;
    }

    public void setObjRefsTableName(String objRefsTableName) {
        this.objRefsTableName = objRefsTableName;
    }

    public String getAttributesPermanentTableName() {
        return attributesPermanentTableName;
    }

    public void setAttributesPermanentTableName(String attributesPermanentTableName) {
        this.attributesPermanentTableName = attributesPermanentTableName;
    }

    public String getRootTableName() {
        return rootTableName;
    }

    public void setRootTableName(String rootTableName) {
        this.rootTableName = rootTableName;
    }

    public String getAttrTypesPermanentTableName() {
        return attrTypesPermanentTableName;
    }

    public void setAttrTypesPermanentTableName(String attrTypesPermanentTableName) {
        this.attrTypesPermanentTableName = attrTypesPermanentTableName;
    }

    public String getOtid() {
        return otid;
    }

    public void setOtid(String otid) {
        this.otid = otid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getDateAppender() {
        return dateAppender;
    }

    public void setDateAppender(String dateAppender) {
        this.dateAppender = dateAppender;
    }

    public String getEntityTypeIdConstant() {
        return entityTypeIdConstant;
    }

    public void setEntityTypeIdConstant(String entityTypeIdConstant) {
        this.entityTypeIdConstant = entityTypeIdConstant;
    }

    public void setOref(String oref) {
        this.oref = oref;
    }

    public String getOref() {
        return oref;
    }

    public String getRootTypesTableName() {
        return rootTypesTableName;
    }

    public void setRootTypesTableName(String rootTypesTableName) {
        this.rootTypesTableName = rootTypesTableName;
    }

    public String getEntityOrderConstant() {
        return entityOrderConstant;
    }

    public void setEntityOrderConstant(String entityOrderConstant) {
        this.entityOrderConstant = entityOrderConstant;
    }

    public String getEntityIdConstant() {
        return entityIdConstant;
    }

    public void setEntityIdConstant(String entityIdConstant) {
        this.entityIdConstant = entityIdConstant;
    }

    public String getReferenceTypePermanentTableName() {
        return referenceTypePermanentTableName;
    }

    public void setReferenceTypePermanentTableName(String referenceTypePermanentTableName) {
        this.referenceTypePermanentTableName = referenceTypePermanentTableName;
    }

    public String getOtidref() {
        return otidref;
    }

    public void setOtidref(String otidref) {
        this.otidref = otidref;
    }

    public String getNewLineAnd() {
        return newLineAnd;
    }

    public String getSpacedOr() {
        return spacedOr;
    }

    public String getParamObjectId() {
        return paramObjectId;
    }

    public String getParamParentId() {
        return paramParentId;
    }

    public char getLbracketChar() {
        return lbracketChar;
    }

    public char getRbracketChar() {
        return rbracketChar;
    }

    public char getNewLineChar() {
        return newLineChar;
    }

    public char getPointChar() {
        return pointChar;
    }

    public char getDoubleBracket() {
        return doubleBracket;
    }

    public char getSpaceChar() {
        return spaceChar;
    }

    public String getSelectWord() {
        return selectWord;
    }

    public String getFromWord() {
        return fromWord;
    }

    public String getWhereWord() {
        return whereWord;
    }

    public String getMapEqualitySign() {
        return mapEqualitySign;
    }

    public char getEqualitySign() {
        return equalitySign;
    }

    public char getSingleBracket() {
        return singleBracket;
    }
}
