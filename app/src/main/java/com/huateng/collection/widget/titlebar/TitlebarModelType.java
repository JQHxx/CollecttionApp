package com.huateng.collection.widget.titlebar;

/**
 * author: Devin createTime:2015年7月10日 desciprion:
 */
public class TitlebarModelType {

  public static final int HIDE = 0x08;
  public static final int CUSTOM = 0x11;

  public static class ARROW {
    public static final int DOWN = 0x09;
    public static final int LEFT = 0x010;

  }

  public static class TEXT {
    public static final int PLAIN = 0x03;

    public static class ARROW {
      public static final int DOWN = 0x01;
      public static final int LEFT = 0x02;

    }
  }

  public static class IMAGE {
    public static final int PLAIN = 0x04;
    public static final int TEXT = 0x05;

    public static class ARROW {
      public static final int DOWN = 0x06;
      public static final int LEFT = 0x07;

    }
  }

}
