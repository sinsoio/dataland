package io.sinso.dataland.util;


import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.4.
 */
@SuppressWarnings("rawtypes")
public class NFTERC721 extends Contract {
    public static final String BINARY = "[\n"
            + "\t{\n"
            + "\t\t\"constant\": true,\n"
            + "\t\t\"inputs\": [],\n"
            + "\t\t\"name\": \"name\",\n"
            + "\t\t\"outputs\": [\n"
            + "\t\t\t{\n"
            + "\t\t\t\t\"name\": \"_name\",\n"
            + "\t\t\t\t\"type\": \"string\"\n"
            + "\t\t\t}\n"
            + "\t\t],\n"
            + "\t\t\"payable\": false,\n"
            + "\t\t\"stateMutability\": \"view\",\n"
            + "\t\t\"type\": \"function\"\n"
            + "\t},\n"
            + "\t{\n"
            + "\t\t\"constant\": true,\n"
            + "\t\t\"inputs\": [],\n"
            + "\t\t\"name\": \"symbol\",\n"
            + "\t\t\"outputs\": [\n"
            + "\t\t\t{\n"
            + "\t\t\t\t\"name\": \"_symbol\",\n"
            + "\t\t\t\t\"type\": \"string\"\n"
            + "\t\t\t}\n"
            + "\t\t],\n"
            + "\t\t\"payable\": false,\n"
            + "\t\t\"stateMutability\": \"view\",\n"
            + "\t\t\"type\": \"function\"\n"
            + "\t},\n"
            + "\t{\n"
            + "\t\t\"constant\": true,\n"
            + "\t\t\"inputs\": [\n"
            + "\t\t\t{\n"
            + "\t\t\t\t\"name\": \"_tokenId\",\n"
            + "\t\t\t\t\"type\": \"uint256\"\n"
            + "\t\t\t}\n"
            + "\t\t],\n"
            + "\t\t\"name\": \"tokenURI\",\n"
            + "\t\t\"outputs\": [\n"
            + "\t\t\t{\n"
            + "\t\t\t\t\"name\": \"\",\n"
            + "\t\t\t\t\"type\": \"string\"\n"
            + "\t\t\t}\n"
            + "\t\t],\n"
            + "\t\t\"payable\": false,\n"
            + "\t\t\"stateMutability\": \"view\",\n"
            + "\t\t\"type\": \"function\"\n"
            + "\t}\n"
            + "]";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    @Deprecated
    protected NFTERC721(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NFTERC721(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected NFTERC721(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected NFTERC721(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tokenURI(BigInteger _tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static NFTERC721 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NFTERC721(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static NFTERC721 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NFTERC721(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static NFTERC721 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new NFTERC721(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static NFTERC721 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new NFTERC721(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<NFTERC721> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(NFTERC721.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<NFTERC721> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NFTERC721.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<NFTERC721> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(NFTERC721.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<NFTERC721> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NFTERC721.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

}
