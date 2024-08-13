package org.example.object.emailcodes;

public class ValidationEmailScriptter extends EmailScriptter {
    private String company = "test";
    private String companyEmail = "test@gmail.com";


    public ValidationEmailScriptter(String to, String title){
        super(to, title);
    }

    public void sender(String name, String code){
        super.sender(setHtml(name, code));
    }

    private String setHtml(String name, String code){
        return """
            <div style="display:block!important;min-width:100%!important;width:100%!important;margin:0;padding:0;background-color:#F1F5F9;" bgcolor="#F1F5F9">
                <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" style="width:100%;margin:0;padding:0;border-spacing:0;border-collapse:collapse;">
                    <tbody>
                        <tr>
                            <td style="margin:0;padding:32px">
                                <table align="center" cellpadding="0" cellspacing="0" border="0" style="max-width:640px;margin:0 auto;padding:0;border-spacing:0;border-collapse:collapse;background-color:#ffffff;">
                                    <tbody>
                                        <tr>
                                            <td style="padding:20px 24px;background-color:#1E293B;line-height:1;">
                                                <img src="https://onus-common-static.s3.amazonaws.com/logo/onus-logo-w.svg" width="100" alt="ONUS">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding:48px 0 24px;color:#1E293B;font-size:16px;line-height:1.5em;font-weight:400;text-align:center;font-family:'Apple SD Gothic Neo','Pretendard','Noto Sans KR','Spoqa Han Sans Neo','Malgun Gothic','dotum','sans-serif';"
                                                align="center">
                                                KPOP STAR 회원가입 이메일 인증번호입니다.
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding:0;padding-bottom:24px;text-align:center;" align="center">
                                                <strong style="display:inline-block;padding:16px 32px;border:0;border-radius:8px;background-color:#F1F5F9;color:#00bcd4;font-size:24px;font-weight:700;white-space:nowrap;letter-spacing:0.1em;font-family:'Apple SD Gothic Neo','Pretendard','Noto Sans KR','Spoqa Han Sans Neo','Malgun Gothic','dotum','sans-serif'">
                                                    ${code}
                                                </strong>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding:16px;" align="center">
                                                <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" style="width:100%;margin:0;padding:0;border-spacing:0;border-collapse:collapse;">
                                                    <tbody>
                                                        <tr>
                                                            <td style="padding:20px 8px 0;border-top:1px solid #E2E8F0;color:#728299;font-size:12px;line-height:1.5em;font-weight:400;font-family:'Apple SD Gothic Neo','Pretendard','Noto Sans KR','Spoqa Han Sans Neo','Malgun Gothic','dotum','sans-serif';">
                                                                본 메일은 발신전용이므로 본 메일로 회신하실 경우 답변이 되지 않습니다.<br />
                                                                서비스 이용 및 법적 의무 고지사항 안내메일로 수신동의 여부와 관계없이 발송드립니다.<br />
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding:12px 8px 20px;color:#728299;font-size:12px;line-height:1.5em;font-weight:400;font-family:'Apple SD Gothic Neo','Pretendard','Noto Sans KR','Spoqa Han Sans Neo','Malgun Gothic','dotum','sans-serif';">
                                                                    (주)${company} <span style="margin:0 2px;color:#CCD5E0">|</span> 경기도 하남시 <br />
                                                                    TEL : 010-000-0000 <span style="margin:0 2px;color:#CCD5E0">|</span> E-MAIL : <a href="mailto:${companyEmail}" target="_blank" style="color:inherit">${companyEmail}</a><br />
                                                                    Copyright©2022 ${company} All rights reserved.<br />
                                                                </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        """.replace("${name}", name)
           .replace("${code}", code)
           .replace("${company}", company)
           .replace("${companyEmail}", companyEmail)
            ;
    }



}
