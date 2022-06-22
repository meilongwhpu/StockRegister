package com.cstc.stockregister.entity;


import lombok.Data;

@Data
public class OrgInfoChangeDTO {
    private int serialNum;
    private String orgName;
    private String orgCreditCode;
    private String legalPerson;
    private String legalIdNumber;
    private String registeredAddress;
    private String registryDate;
    private String officeAddress;
    private String contactNumber;
    private Integer credType;
    private Integer orgType;
    private Integer registeredCapital;
    private Integer paidInCapital;
    private Integer shareholdersNumber;
    private Integer legalIdType;

}
