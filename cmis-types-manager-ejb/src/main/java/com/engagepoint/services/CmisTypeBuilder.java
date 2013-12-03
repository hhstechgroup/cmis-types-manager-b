package com.engagepoint.services;

import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import java.util.List;
import java.util.Map;

/**
 * User: Qnex
 * Date: 01.12.13
 * Time: 13:14
 */
public class CmisTypeBuilder {
    private TypeDefinition type;
    private Prototype prototype;

    public void buildType() {
        type = new TypeDefinition() {
            private List<CmisExtensionElement> extensions;

            @Override
            public String getId() {
                return prototype.getId();
            }

            @Override
            public String getLocalName() {
                return prototype.getLocalName();
            }

            @Override
            public String getLocalNamespace() {
                return prototype.getLocalNamespace();
            }

            @Override
            public String getDisplayName() {
                return prototype.getDisplayName();
            }

            @Override
            public String getQueryName() {
                return prototype.getQueryName();
            }

            @Override
            public String getDescription() {
                return prototype.getDescription();
            }

            @Override
            public BaseTypeId getBaseTypeId() {
                return BaseTypeId.fromValue(prototype.getBaseTypeId());
            }

            @Override
            public String getParentTypeId() {
                return prototype.getParentTypeId();
            }

            @Override
            public Boolean isCreatable() {
                return prototype.isCreatable();
            }

            @Override
            public Boolean isFileable() {
                return prototype.isFileable();
            }

            @Override
            public Boolean isQueryable() {
                return prototype.isQueryable();
            }

            @Override
            public Boolean isFulltextIndexed() {
                return prototype.isFulltextIndexed();
            }

            @Override
            public Boolean isIncludedInSupertypeQuery() {
                return prototype.isIncludedInSupertypeQuery();
            }

            @Override
            public Boolean isControllablePolicy() {
                return prototype.isControllablePolicy();
            }

            @Override
            public Boolean isControllableAcl() {
                return prototype.isControllableAcl();
            }

            @Override
            public Map<String, PropertyDefinition<?>> getPropertyDefinitions() {
                return null;
            }

            @Override
            public TypeMutability getTypeMutability() {
                return new TypeMutability() {
                    @Override
                    public Boolean canCreate() {
                        return true;
                    }

                    @Override
                    public Boolean canUpdate() {
                        return true;
                    }

                    @Override
                    public Boolean canDelete() {
                        return true;
                    }

                    @Override
                    public List<CmisExtensionElement> getExtensions() {
                        return null;
                    }

                    @Override
                    public void setExtensions(List<CmisExtensionElement> extensions) {
                    }
                };
            }

            @Override
            public List<CmisExtensionElement> getExtensions() {
                return null;
            }

            @Override
            public void setExtensions(List<CmisExtensionElement> extensions) {
                this.extensions = extensions;
            }
        };
    }

    public TypeDefinition getType() {
        return type;
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }
}
