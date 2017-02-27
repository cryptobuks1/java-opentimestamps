import java.util.HashMap;
import java.util.Map;

/**
 * Timestamp proof operations.
 * Operations are the edges in the timestamp tree, with each operation taking a message and zero or more arguments to produce a result.
 */
class Op {

    /**
     * Maximum length of an Op result
     *
     * For a verifier, this limit is what limits the maximum amount of memory you
     * need at any one time to verify a particular timestamp path; while verifying
     * a particular commitment operation path previously calculated results can be
     * discarded.
     *
     * Of course, if everything was a merkle tree you never need to append/prepend
     * anything near 4KiB of data; 64 bytes would be plenty even with SHA512. The
     * main need for this is compatibility with existing systems like Bitcoin
     * timestamps and Certificate Transparency servers. While the pathological
     * limits required by both are quite large - 1MB and 16MiB respectively - 4KiB
     * is perfectly adequate in both cases for more reasonable usage.
     *
     * Op subclasses should set this limit even lower if doing so is appropriate
     * for them.
     */
    public static int _MAX_RESULT_LENGTH = 4096;


    /**
     * Maximum length of the message an Op can be applied too.
     *
     * Similar to the result length limit, this limit gives implementations a sane
     * constraint to work with; the maximum result-length limit implicitly
     * constrains maximum message length anyway.
     *
     * Op subclasses should set this limit even lower if doing so is appropriate
     * for them.
     */
    public static int _MAX_MSG_LENGTH = 4096;

    public static byte _TAG= (byte)0x00;

    public String _TAG_NAME() {
        return "";
    }

    /**
     * Deserialize operation from a buffer.
     * @param {StreamDeserializationContext} ctx - The stream deserialization context.
     * @return {Op} The subclass Operation.
     */
    public static Op deserialize(StreamDeserializationContext ctx) {
        byte tag = ctx.readBytes(1)[0];
        return Op.deserializeFromTag(ctx, tag);
    }

    /**
     * Deserialize operation from a buffer.
     * @param {StreamDeserializationContext} ctx - The stream deserialization context.
     * @param {int} tag - The tag of the operation.
     * @return {Op} The subclass Operation.
     */
    public static Op deserializeFromTag(StreamDeserializationContext ctx, byte tag) {
        if (tag == OpAppend._TAG){
            return OpAppend.deserializeFromTag(ctx,tag);
        }else if (tag == OpPrepend._TAG){
            return OpPrepend.deserializeFromTag(ctx,tag);
        }else if (tag == OpSHA1._TAG){
            return OpSHA1.deserializeFromTag(ctx,tag);
        }else if (tag == OpSHA256._TAG){
            return OpSHA256.deserializeFromTag(ctx,tag);
        }else if (tag == OpRIPEMD160._TAG){
            return OpRIPEMD160.deserializeFromTag(ctx,tag);
        }else {
            System.err.print("Unknown operation tag: " + tag);
            return null;
        }
    }

    /**
     * Serialize operation.
     * @param {StreamSerializationContext} ctx - The stream serialization context.
     */
    void serialize(StreamSerializationContext ctx) {
        ctx.writeByte(_TAG);
    }

    /**
     * Apply the operation to a message.
     * Raises MsgValueError if the message value is invalid, such as it being
     * too long, or it causing the result to be too long.
     * @param {byte[]} msg - The message.
     */
    byte[] call(byte[] msg) {
        if (msg.length > _MAX_MSG_LENGTH) {
            System.err.print("Error : Message too long;");
            return null;
        }

        byte[] r = this.call(msg);

        if (r.length > _MAX_RESULT_LENGTH) {
            System.err.print("Error : Result too long;");
        }
        return r;
    }
}