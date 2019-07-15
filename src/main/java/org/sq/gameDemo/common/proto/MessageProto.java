// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageProto.proto

package org.sq.gameDemo.common.proto;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Msg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>uint64 msg_id = 1;</code>
     */
    long getMsgId();

    /**
     * <pre>
     *发送方命令
     * </pre>
     *
     * <code>string cmd = 2;</code>
     */
    String getCmd();
    /**
     * <pre>
     *发送方命令
     * </pre>
     *
     * <code>string cmd = 2;</code>
     */
    com.google.protobuf.ByteString
        getCmdBytes();

    /**
     * <pre>
     *时间戳(单位:毫秒)
     * </pre>
     *
     * <code>uint64 time = 3;</code>
     */
    long getTime();

    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>string content = 4;</code>
     */
    String getContent();
    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>string content = 4;</code>
     */
    com.google.protobuf.ByteString
        getContentBytes();

    /**
     * <pre>
     *验证信息
     * </pre>
     *
     * <code>string token = 5;</code>
     */
    String getToken();
    /**
     * <pre>
     *验证信息
     * </pre>
     *
     * <code>string token = 5;</code>
     */
    com.google.protobuf.ByteString
        getTokenBytes();

    /**
     * <code>int32 result = 6;</code>
     */
    int getResult();
  }
  /**
   * Protobuf type {@code Msg}
   */
  public  static final class Msg extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:Msg)
      MsgOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Msg.newBuilder() to construct.
    private Msg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Msg() {
      cmd_ = "";
      content_ = "";
      token_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new Msg();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Msg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              msgId_ = input.readUInt64();
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              cmd_ = s;
              break;
            }
            case 24: {

              time_ = input.readUInt64();
              break;
            }
            case 34: {
              String s = input.readStringRequireUtf8();

              content_ = s;
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              token_ = s;
              break;
            }
            case 48: {

              result_ = input.readInt32();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MessageProto.internal_static_Msg_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MessageProto.internal_static_Msg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Msg.class, Builder.class);
    }

    public static final int MSG_ID_FIELD_NUMBER = 1;
    private long msgId_;
    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>uint64 msg_id = 1;</code>
     */
    public long getMsgId() {
      return msgId_;
    }

    public static final int CMD_FIELD_NUMBER = 2;
    private volatile Object cmd_;
    /**
     * <pre>
     *发送方命令
     * </pre>
     *
     * <code>string cmd = 2;</code>
     */
    public String getCmd() {
      Object ref = cmd_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        cmd_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *发送方命令
     * </pre>
     *
     * <code>string cmd = 2;</code>
     */
    public com.google.protobuf.ByteString
        getCmdBytes() {
      Object ref = cmd_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        cmd_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TIME_FIELD_NUMBER = 3;
    private long time_;
    /**
     * <pre>
     *时间戳(单位:毫秒)
     * </pre>
     *
     * <code>uint64 time = 3;</code>
     */
    public long getTime() {
      return time_;
    }

    public static final int CONTENT_FIELD_NUMBER = 4;
    private volatile Object content_;
    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>string content = 4;</code>
     */
    public String getContent() {
      Object ref = content_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        content_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>string content = 4;</code>
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TOKEN_FIELD_NUMBER = 5;
    private volatile Object token_;
    /**
     * <pre>
     *验证信息
     * </pre>
     *
     * <code>string token = 5;</code>
     */
    public String getToken() {
      Object ref = token_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        token_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *验证信息
     * </pre>
     *
     * <code>string token = 5;</code>
     */
    public com.google.protobuf.ByteString
        getTokenBytes() {
      Object ref = token_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        token_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RESULT_FIELD_NUMBER = 6;
    private int result_;
    /**
     * <code>int32 result = 6;</code>
     */
    public int getResult() {
      return result_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (msgId_ != 0L) {
        output.writeUInt64(1, msgId_);
      }
      if (!getCmdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, cmd_);
      }
      if (time_ != 0L) {
        output.writeUInt64(3, time_);
      }
      if (!getContentBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, content_);
      }
      if (!getTokenBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, token_);
      }
      if (result_ != 0) {
        output.writeInt32(6, result_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (msgId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(1, msgId_);
      }
      if (!getCmdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, cmd_);
      }
      if (time_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(3, time_);
      }
      if (!getContentBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, content_);
      }
      if (!getTokenBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, token_);
      }
      if (result_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, result_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof Msg)) {
        return super.equals(obj);
      }
      Msg other = (Msg) obj;

      if (getMsgId()
          != other.getMsgId()) return false;
      if (!getCmd()
          .equals(other.getCmd())) return false;
      if (getTime()
          != other.getTime()) return false;
      if (!getContent()
          .equals(other.getContent())) return false;
      if (!getToken()
          .equals(other.getToken())) return false;
      if (getResult()
          != other.getResult()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + MSG_ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getMsgId());
      hash = (37 * hash) + CMD_FIELD_NUMBER;
      hash = (53 * hash) + getCmd().hashCode();
      hash = (37 * hash) + TIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTime());
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (37 * hash) + TOKEN_FIELD_NUMBER;
      hash = (53 * hash) + getToken().hashCode();
      hash = (37 * hash) + RESULT_FIELD_NUMBER;
      hash = (53 * hash) + getResult();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static Msg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Msg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Msg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Msg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Msg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Msg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Msg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Msg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Msg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static Msg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Msg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Msg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Msg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Msg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Msg)
        MsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return MessageProto.internal_static_Msg_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return MessageProto.internal_static_Msg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                Msg.class, Builder.class);
      }

      // Construct using org.sq.gameDemo.common.proto.MessageProto.Msg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        msgId_ = 0L;

        cmd_ = "";

        time_ = 0L;

        content_ = "";

        token_ = "";

        result_ = 0;

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageProto.internal_static_Msg_descriptor;
      }

      @Override
      public Msg getDefaultInstanceForType() {
        return Msg.getDefaultInstance();
      }

      @Override
      public Msg build() {
        Msg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public Msg buildPartial() {
        Msg result = new Msg(this);
        result.msgId_ = msgId_;
        result.cmd_ = cmd_;
        result.time_ = time_;
        result.content_ = content_;
        result.token_ = token_;
        result.result_ = result_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Msg) {
          return mergeFrom((Msg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Msg other) {
        if (other == Msg.getDefaultInstance()) return this;
        if (other.getMsgId() != 0L) {
          setMsgId(other.getMsgId());
        }
        if (!other.getCmd().isEmpty()) {
          cmd_ = other.cmd_;
          onChanged();
        }
        if (other.getTime() != 0L) {
          setTime(other.getTime());
        }
        if (!other.getContent().isEmpty()) {
          content_ = other.content_;
          onChanged();
        }
        if (!other.getToken().isEmpty()) {
          token_ = other.token_;
          onChanged();
        }
        if (other.getResult() != 0) {
          setResult(other.getResult());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        Msg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Msg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long msgId_ ;
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public long getMsgId() {
        return msgId_;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public Builder setMsgId(long value) {
        
        msgId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public Builder clearMsgId() {
        
        msgId_ = 0L;
        onChanged();
        return this;
      }

      private Object cmd_ = "";
      /**
       * <pre>
       *发送方命令
       * </pre>
       *
       * <code>string cmd = 2;</code>
       */
      public String getCmd() {
        Object ref = cmd_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          cmd_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *发送方命令
       * </pre>
       *
       * <code>string cmd = 2;</code>
       */
      public com.google.protobuf.ByteString
          getCmdBytes() {
        Object ref = cmd_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          cmd_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *发送方命令
       * </pre>
       *
       * <code>string cmd = 2;</code>
       */
      public Builder setCmd(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        cmd_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送方命令
       * </pre>
       *
       * <code>string cmd = 2;</code>
       */
      public Builder clearCmd() {
        
        cmd_ = getDefaultInstance().getCmd();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送方命令
       * </pre>
       *
       * <code>string cmd = 2;</code>
       */
      public Builder setCmdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        cmd_ = value;
        onChanged();
        return this;
      }

      private long time_ ;
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public long getTime() {
        return time_;
      }
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public Builder setTime(long value) {
        
        time_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public Builder clearTime() {
        
        time_ = 0L;
        onChanged();
        return this;
      }

      private Object content_ = "";
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>string content = 4;</code>
       */
      public String getContent() {
        Object ref = content_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          content_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>string content = 4;</code>
       */
      public com.google.protobuf.ByteString
          getContentBytes() {
        Object ref = content_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          content_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>string content = 4;</code>
       */
      public Builder setContent(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>string content = 4;</code>
       */
      public Builder clearContent() {
        
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>string content = 4;</code>
       */
      public Builder setContentBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        content_ = value;
        onChanged();
        return this;
      }

      private Object token_ = "";
      /**
       * <pre>
       *验证信息
       * </pre>
       *
       * <code>string token = 5;</code>
       */
      public String getToken() {
        Object ref = token_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          token_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *验证信息
       * </pre>
       *
       * <code>string token = 5;</code>
       */
      public com.google.protobuf.ByteString
          getTokenBytes() {
        Object ref = token_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          token_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *验证信息
       * </pre>
       *
       * <code>string token = 5;</code>
       */
      public Builder setToken(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        token_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *验证信息
       * </pre>
       *
       * <code>string token = 5;</code>
       */
      public Builder clearToken() {
        
        token_ = getDefaultInstance().getToken();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *验证信息
       * </pre>
       *
       * <code>string token = 5;</code>
       */
      public Builder setTokenBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        token_ = value;
        onChanged();
        return this;
      }

      private int result_ ;
      /**
       * <code>int32 result = 6;</code>
       */
      public int getResult() {
        return result_;
      }
      /**
       * <code>int32 result = 6;</code>
       */
      public Builder setResult(int value) {
        
        result_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 result = 6;</code>
       */
      public Builder clearResult() {
        
        result_ = 0;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:Msg)
    }

    // @@protoc_insertion_point(class_scope:Msg)
    private static final Msg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Msg();
    }

    public static Msg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Msg>
        PARSER = new com.google.protobuf.AbstractParser<Msg>() {
      @Override
      public Msg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Msg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Msg> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Msg> getParserForType() {
      return PARSER;
    }

    @Override
    public Msg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Msg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Msg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\022MessageProto.proto\"`\n\003Msg\022\016\n\006msg_id\030\001 " +
      "\001(\004\022\013\n\003cmd\030\002 \001(\t\022\014\n\004time\030\003 \001(\004\022\017\n\007conten" +
      "t\030\004 \001(\t\022\r\n\005token\030\005 \001(\t\022\016\n\006result\030\006 \001(\005B," +
      "\n\034org.sq.gameDemo.common.protoB\014MessageP" +
      "rotob\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_Msg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Msg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Msg_descriptor,
        new String[] { "MsgId", "Cmd", "Time", "Content", "Token", "Result", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
