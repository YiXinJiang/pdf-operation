package com.jyx.pdf.stamp;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.jyx.pdf.entity.SignatureInfo;
import com.jyx.pdf.utils.ItextUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import static com.jyx.pdf.utils.ItextUtil.PASSWORD;

/**
 * @ClassName: Test
 * @Description:
 * @Author: jyx
 * @Date: 2024-03-27 10:32
 **/
public class Test {

    final static String pkPath = "src/main/resources/jyx.p12";
    final static String stampImgPath = "D:\\project\\pdf-operation\\src\\main\\resources\\smyt.png";

    public static void main(String[] args) {
        try {
            // 需要进行签章的pdf
            String sourcePath = "E:\\pdf\\nine.pdf";
            String targetPath = "E:\\pdf\\nine_stamp.pdf";

            // 将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(Files.newInputStream(Paths.get(pkPath)), PASSWORD);
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
            // 得到证书链
            Certificate[] chain = ks.getCertificateChain(alias);

            // 封装签章信息
            SignatureInfo signInfo = new SignatureInfo();
            signInfo.setReason("Appointed by heaven to live forever");
            signInfo.setLocation("location");
            signInfo.setPk(pk);
            signInfo.setChain(chain);
            signInfo.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            signInfo.setDigestAlgorithm(DigestAlgorithms.SHA1);
            signInfo.setFieldName("stamp of king");

            // 签章图片
            signInfo.setImagePath(stampImgPath);
            signInfo.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            // 值越大，代表向x轴坐标平移 缩小 （反之，值越小，印章会放大）
            signInfo.setRectllx(100);
            // 值越大，代表向y轴坐标向上平移（大小不变）
            signInfo.setRectlly(200);
            // 值越大   代表向x轴坐标向右平移（大小不变）
            signInfo.setRecturx(150);
            // 值越大，代表向y轴坐标向上平移（大小不变）
            signInfo.setRectury(150);

            // 签章后的pdf路径
            ItextUtil.sign(sourcePath, targetPath, signInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
