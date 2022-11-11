package io.sinso.dataland.util;

import com.alibaba.fastjson.JSONObject;
import io.sinso.dataland.vo.file.NftParsingUrlVo;

import java.util.Map;

/**
 * @author hengbol
 * @date 7/1/22 4:56 PM
 */
public class OpenSeaUtil {


    public static NftParsingUrlVo getUrl(Map data) {
        NftParsingUrlVo nftParsingUrlVo = new NftParsingUrlVo();
        if (data.get("image_uri") != null) {
            String contentUri = data.get("content_uri").toString();
            String imageUri = data.get("image_uri").toString();
            nftParsingUrlVo.setImageUrl(parsingImage(contentUri));
            nftParsingUrlVo.setLogo(parsingImage(imageUri));
            return nftParsingUrlVo;
        }
//        if (jsonObj == null) {
//            if (data.get("link") != null) {
//                nftParsingUrlVo.setImageUrl("https://ipfs.io/ipfs/" + data.get("link"));
//            }
//            if (data.get("nft_platform_logo") != null) {
//                nftParsingUrlVo.setLogo(data.get("nft_platform_logo").toString());
//            }
//            return nftParsingUrlVo;
//        }
//        String json = jsonObj.toString();
//        if (nftFormat.contains("video") || json.contains("animation_url")) {
//            nftParsingUrlVo.setImageUrl(parsingVideo(json));
//        } else {
//            nftParsingUrlVo.setImageUrl(parsingImage(json));
//        }
//        nftParsingUrlVo.setLogo(parsingImage(json));
        return nftParsingUrlVo;
    }


    /**
     *
     * @param json
     * @return
     */
    private static String parsingVideo(String json) {
        String imageUrl = null;
        if (JSONObject.parseObject(json).get("animation_url") == null) {
            return parsingImage(json);
        }
        String image = JSONObject.parseObject(json).get("animation_url").toString();
        if (image.contains("https")) {
            imageUrl = image;
            if (imageUrl.contains("https://storage.opensea.io/files")) {
                String[] split1 = imageUrl.split("/");
                imageUrl = "https://openseauserdata.com/files/" + split1[split1.length - 1];
            }
        }
        return imageUrl;
    }

    /**
     *
     * @param image
     * @return
     */
    private static String parsingImage(String image) {
        String imageUrl;
//        String image = JSONObject.parseObject(json).get("image").toString();
        if (image.length() > 500) {
            return image;
        }
        if (image.contains("https")) {
            return image;
        } else {
            if (image.contains("ipfs://")) {
                imageUrl = "https://ipfs.io/ipfs/" + image.split("ipfs://")[1];
            } else {
                imageUrl = "https://ipfs.io/ipfs/" + image;
            }
        }

        return imageUrl;
    }
}
