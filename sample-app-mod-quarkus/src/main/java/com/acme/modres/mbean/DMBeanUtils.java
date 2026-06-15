package com.acme.modres.mbean;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanOperationInfo;

public final class DMBeanUtils {
  private static final Logger logger = Logger.getLogger(DMBeanUtils.class.getName());

  public static MBeanOperationInfo[] getOps(OpMetadataList opList) {
    MBeanOperationInfo[] ops = null;
    if (opList == null || opList.getOpMetadatList() == null) {
      logger.log(Level.WARNING, "No operation is configured");
      return ops;
    }

    int numOps = opList.getOpMetadatList().size();
    if (numOps > 0) {
      ops = new MBeanOperationInfo[numOps];
      int i = 0;
      for (OpMetadata opMetadata : opList.getOpMetadatList()) {
        String name = opMetadata.getName();
        String desc = opMetadata.getDescription();
        String type = opMetadata.getType();
        int impact = normalizeImpact(opMetadata.getImpact());

        MBeanOperationInfo opInfo = new MBeanOperationInfo(name, desc, /* signature */ null, type, impact, /*
                                                                                                            * descriptor
                                                                                                            */ null);
        ops[i++] = opInfo;
      }
    }

    return ops;
  }

  /**
   * MBeanOperationInfo only accepts INFO(0), ACTION(1), ACTION_INFO(2) or
   * UNKNOWN(3). The legacy ops.json carried out-of-range values (e.g. 10) that
   * WebSphere tolerated but the standard JDK rejects with
   * IllegalArgumentException. Clamp anything invalid to UNKNOWN so the MBean
   * still registers instead of breaking servlet initialization.
   */
  private static int normalizeImpact(int impact) {
    if (impact == MBeanOperationInfo.INFO
        || impact == MBeanOperationInfo.ACTION
        || impact == MBeanOperationInfo.ACTION_INFO
        || impact == MBeanOperationInfo.UNKNOWN) {
      return impact;
    }
    logger.log(Level.WARNING, "Invalid MBean operation impact {0}, defaulting to UNKNOWN", impact);
    return MBeanOperationInfo.UNKNOWN;
  }
}
