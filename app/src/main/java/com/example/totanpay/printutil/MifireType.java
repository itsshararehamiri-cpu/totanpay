package com.example.totanpay.printutil;

public class MifireType {

    static int MIFIRE_CLASSIC_1K=0;
    static int MIFIRE_CLASSIC_2K=1;
    static int MIFIRE_CLASSIC_4K=2;
    static int MIFIRE_MINI=3;
    static int MIFIRE_PLUS=4;
    static int MIFIRE_UltraLight=5;
    static int MIFIRE_UltraLight_C=6;
    static int MIFIRE_UltraLight_EV1=7;
    static int MIFIRE_DESFire_EV=8;
    static int MIFIRE_DESFire_Light=9;
    static int MIFIRE_UNKNOWN=10;



    static int ParseMifireType(byte[] sak,byte[] ats,byte[] version,boolean isAuth)
    {
        int mifireType=MIFIRE_UNKNOWN;

        if((sak[0]&0x02)==0x02) //SAK b2=1?
        {
            mifireType=MIFIRE_UNKNOWN;
        }
        else
        {
            if((sak[0]&0x08)==0x08)//SAK b4=1?
            {
                if((sak[0]&0x10)==0x10)//SAK b5=1?
                {
                    if((sak[0]&0x01)==0x01)//SAK b1=1?
                    {
                        mifireType=MIFIRE_CLASSIC_2K;
                    }
                    else
                    {
                        if((sak[0]&0x20)==0x20)//SAK b6=1?
                        {
                            mifireType=MIFIRE_CLASSIC_4K;
                        }
                        else
                        {
                            //RATS
                            if(version!=null)
                            {
                                if((ats[1]==0x02)||(ats[1]==0x82))
                                {
                                    mifireType=MIFIRE_PLUS;
                                }
                            }
                            else
                            {
                                mifireType=MIFIRE_PLUS;
                            }

                        }

                    }

                }
                else
                {
                    if((sak[0]&0x01)==0x01)//SAK b1=1?
                    {
                        mifireType=MIFIRE_MINI;
                    }
                    else
                    {
                        if((sak[0]&0x20)==0x20)//SAK b6=1?
                        {
                            mifireType=MIFIRE_CLASSIC_1K;
                        }
                        else
                        {
                            //RATS
                            if(version!=null)
                            {
                                if((ats[1]==0x02)||(ats[1]==0x82))
                                {
                                    mifireType=MIFIRE_PLUS;
                                }
                            }
                            else
                            {
                                mifireType=MIFIRE_PLUS;
                            }

                        }
                    }

                }
            }
            else
            {
                if((sak[0]&0x10)==0x10)//SAK b5=1?
                {

                    mifireType=MIFIRE_PLUS;

                }
                else
                {
                    if((sak[0]&0x01)==0x01)//SAK b1=1?
                    {
                        mifireType=MIFIRE_UNKNOWN;
                    }
                    else
                    {
                        if((sak[0]&0x20)==0x20)//SAK b6=1?
                        {

                            if(version!=null)
                            {
                                if((ats[1]==0x02)||(ats[1]==0x82))
                                {
                                    mifireType=MIFIRE_PLUS;
                                }
                                else if((ats[1]==0x01)||(ats[1]==0x81))
                                {
                                    mifireType=MIFIRE_DESFire_EV;
                                }
                                else if(ats[1]==0x08)
                                {
                                    mifireType=MIFIRE_DESFire_Light;
                                }
                                else if(ats[1]==0x03)
                                {
                                    //NTAG_4XX
                                }


                            }
                            else
                            {
                                mifireType=MIFIRE_PLUS;
                            }
                        }
                        else
                        {
                            //RATS
                            if(version!=null)
                            {
                                if(ats[1]==0x03)
                                {
                                    mifireType=MIFIRE_UltraLight_EV1;
                                }
                                else if(ats[1]==0x03)
                                {
                                    //NTAG_2XX
                                }
                            }
                            else
                            {
                               if(isAuth)
                               {
                                   mifireType=MIFIRE_UltraLight_C;
                               }
                               else
                               {
                                   mifireType=MIFIRE_UltraLight;
                               }
                            }

                        }

                    }
                }

            }


        }

        return mifireType;

    }






}
