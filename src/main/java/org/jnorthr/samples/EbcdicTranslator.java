/**
 *  -------------------------------------------------------------------------
 *                                 Copyright
 *
 *               International Financial Data Services Limited 2003
 *                            All rights reserved.
 *
 *
 *  No part of this document may be reproduced, stored in a retrieval
 *  system, or transmitted, in any form or by any means, electronic,
 *  mechanical, photocopying, networking or otherwise, without the prior
 *  written permission of International Financial Data Services Limited.
 *
 *  The computer system, procedures, data bases, and programs created and
 *  maintained by International Financial Data Services Limited are proprietary
 *  in nature and as such are confidential. Any unauthorised use, misuse, or
 *  disclosure of such items or information may result in civil liabilities
 *  and may be subject to criminal penalties under the applicable laws.
 *  -------------------------------------------------------------------------
 *
 *  @author :  David Hu - 08th May 2003
 *
 * $Log:   //ifpvcssrv1/web_arc/web2/service/SERVICE-INF/classes/uk/co/ifdsgroup/service/viewmanager/viewdata/EbcdicTranslator.java-arc  $
 * 
 *    Rev 1.1   Sep 30 2003 15:34:30   IVESPAUL
 * Added continuation data processing, and performance tuning.
 *
 *    Rev 1.0   18 Sep 2003 11:02:18   PIves
 * Initial revision.
 *
*/

/**
 * Package that class belongs to.
*/
package uk.co.ifdsgroup.service.viewmanager.viewdata;

/**
 * System imports
*/
import java.io.*;
import java.lang.String;

/**
 * Singleton class without using synchronization.
*/
public class EbcdicTranslator
{
  private static final EbcdicTranslator _instance = new EbcdicTranslator();

  private EbcdicTranslator()
  {
  }

  /**
   * get the singleton object
   *
   * @return the reference of this class object
  */
  public static EbcdicTranslator getInstance()
  {
    return _instance;
  }

  /**
   * ASCII <=> EBCDIC conversion method
   *
   * @param string strBuffer
   * @return string
   */
  public String cnvASCIItoEBCDIC( String strBuffer )
  {
    return( cnvASCIItoEBCDIC( stringtobytes( strBuffer ) ) );
  }

  /**
   * ASCII <=> EBCDIC conversion method
   *
   * @param byte array
   * @return string
   */
  public String cnvASCIItoEBCDIC( byte[] byteBuffer )
  {
    String strReturn = null;

    if ( ( byteBuffer != null ) && ( byteBuffer.length > 0 ) )
    {
      StringBuffer sbTemp = new StringBuffer( byteBuffer.length );

      for ( int iPos = 0; iPos < byteBuffer.length; iPos++ )
      {
        int i = (int)byteBuffer[iPos];

        if ( i < 0 )
        {
          i += 256;
        }

        sbTemp.append( chASCII2EBCDIC[ i ] );
      }

      strReturn = new String( sbTemp );
    }

    return( strReturn );
  }

  /**
   * EBCDIC <=> ASCII conversion method
   *
   * @param string strBuffer
   * @return string
  */
  public String cnvEBCDICtoASCII( String strBuffer )
  {
    return( cnvEBCDICtoASCII( stringtobytes( strBuffer ) ) );
  }

  /**
   *  EBCDIC <=> ASCII conversion method
   *
   * @param byte array
   * @return string
  */
  public String cnvEBCDICtoASCII( byte[] byteBuffer )
  {
    String strReturn = null;

    if ( ( byteBuffer != null ) && ( byteBuffer.length > 0 ) )
    {
      StringBuffer sbTemp = new StringBuffer( byteBuffer.length );

      for ( int iPos = 0; iPos < byteBuffer.length; iPos++ )
      {
        int i = (int)byteBuffer[iPos];

        if ( i < 0 )
        {
          i += 256;
        }

        /**
         * convert null to space as vm does.
        */
        if ( byteBuffer[iPos] == 0 )
        {
          byteBuffer[iPos] = 32;
        }

        sbTemp.append( chEBCDICtoASCII[ i ] );
      }

      strReturn = new String( sbTemp );
    }

    return( strReturn );
  }

  /**
   *  string <=> byte array conversion method
   *
   * @param string
   * @return byte array
  */
  public byte[] stringtobytes( String strIn )
  {
    byte byteReturn[] = null;

    if ( ( strIn != null ) && ( strIn.length() > 0 ) )
    {
      int iLength = 0;
      iLength = strIn.length();

      byteReturn = new byte[iLength];

      for ( int iPos = 0; iPos < iLength; iPos++ )
      {
        byteReturn[iPos] = (byte)strIn.charAt(iPos);
      }
    }
    return( byteReturn );
  }

  public boolean isreadable()
  {
    boolean b_readable = true;
    return(b_readable);
  }

  public boolean iswritable()
  {
    boolean b_writable = true;
    return(b_writable);
  }

  /**
   * ASCII <=> EBCDIC conversion table
  */
  static final char chASCII2EBCDIC[] =
  {
    0,  1,  2,  3, 55, 45, 46, 47, 22,  5, 37, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31,
    64, 79,127,123, 91,108, 80,125, 77, 93, 92, 78,107, 96, 75, 97,
    240,241,242,243,244,245,246,247,248,249,122, 94, 76,126,110,111,
    124,193,194,195,196,197,198,199,200,201,209,210,211,212,213,214,
    215,216,217,226,227,228,229,230,231,232,233, 74,224, 90, 95,109,
    121,129,130,131,132,133,134,135,136,137,145,146,147,148,149,150,
    151,152,153,162,163,164,165,166,167,168,169,192,106,208,161,  7,
    32, 33, 34, 35, 36, 21,  6, 23, 40, 41, 42, 43, 44,  9, 10, 27,
    48, 49, 26, 51, 52, 53, 54,  8, 56, 57, 58, 59,  4, 20, 62,225,
    65, 66, 67, 68, 69, 70, 71, 72, 73, 81, 82, 83, 84, 85, 86, 87,
    88, 89, 98, 99,100,101,102,103,104,105,112,113,114,115,116,117,
    118,119,120,128,138,139,140,141,142,143,144,154,155,156,157,158,
    159,160,170,171,172,173,174,175,176,177,178,179,180,181,182,183,
    184,185,186,187,188,189,190,191,202,203,204,205,206,207,218,219,
    220,221,222,223,234,235,236,237,238,239,250,251,252,253,254,255
  };

  /**
   * EBCDIC <=> ASCII conversion table
  */
  static final char chEBCDICtoASCII[] =
  {
    0,  1,  2,  3,156,  9,134,127,151,141,142, 11, 12, 13, 14, 15,
    16, 17, 18, 19,157,133,  8,135, 24, 25,146,143, 28, 29, 30, 31,
    128,129,130,131,132, 10, 23, 27,136,137,138,139,140,  5,  6,  7,
    144,145, 22,147,148,149,150,  4,152,153,154,155, 20, 21,158, 26,
    32,160,161,162,163,164,165,166,167,168, 91, 46, 60, 40, 43, 33,
    38,169,170,171,172,173,174,175,176,177, 93, 36, 42, 41, 59, 94,
    45, 47,178,179,180,181,182,183,184,185,124, 44, 37, 95, 62, 63,
    186,187,188,189,190,191,192,193,194, 96, 58, 35, 64, 39, 61, 34,
    195, 97, 98, 99,100,101,102,103,104,105,196,197,198,199,200,201,
    202,106,107,108,109,110,111,112,113,114,203,204,205,206,207,208,
    209,126,115,116,117,118,119,120,121,122,210,211,212,213,214,215,
    216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,
    123, 65, 66, 67, 68, 69, 70, 71, 72, 73,232,233,234,235,236,237,
    125, 74, 75, 76, 77, 78, 79, 80, 81, 82,238,239,240,241,242,243,
    92,159, 83, 84, 85, 86, 87, 88, 89, 90,244,245,246,247,248,249,
    48, 49, 50, 51, 52, 53, 54, 55, 56, 57,250,251,252,253,254,255
  };
}


