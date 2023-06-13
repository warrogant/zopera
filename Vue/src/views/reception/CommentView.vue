<template>
    <div>
        <div class="w1200">
            <div class="activit_comment">
                <img :src="user.avator" alt="">
                <el-input type="textarea" class="texta" resize="none" size="medium" placeholder="请输入评论内容"
                    v-model="comment.content" maxlength="100"></el-input>
                <el-button type="primary" size="medium" class="butcomment" @click="submit(comment.content)">发表评论</el-button>
            </div>
            <div class="comment_list">
                <div v-for="item in comments" :key="item.id" class="comment_item">
                    <div class="comment_item_content">
                        <div class="comment_item_content_t">
                            <img :src="item.userAvator" alt="" class="avatorImg">
                            <div class="comment_item_content_c">
                                <div>{{ item.username }}</div>
                                <span>{{ item.content }}</span>
                                <span>{{ item.createTime | datetime }} <i
                                        @click="commentReply(item.id, item.username,item)">回复</i>
                                </span>
                                <!-- <span v-if="!isExpanded" @click="expandComments" style="cursor: pointer;">展开</span>
                                <span v-if="isExpanded && item.children.length > maxVisible" @click="collapseComments"
                                    style="cursor: pointer;">收起</span> -->
                            </div>
                        </div>
                        <!-- 回复 -->
                        <div class="comment_reply">
                            <!--  || sub.id < maxVisible  v-show="isExpanded"-->
                            <div v-for="sub in item.children" :key="sub.id">
                                <div class="comment_reply_header">
                                    <img :src="sub.userAvator" alt="" class="avatorImg">
                                    <span>{{ sub.username }}</span>&thinsp;<span>回复<i>@{{ sub.target }}</i></span>:
                                    <span>{{ sub.content }}</span>
                                </div>
                                <div class="commentTime">{{ sub.createTime | datetime }}
                                    <span @click="commentReply(sub.pid, sub.username,item)">回复</span>
                                </div>
                            </div>
                        </div>
                        <div class="activit_comment" v-if="item.comment">
                            <img :src="user.avator" alt="">
                            <el-input type="textarea" class="texta" resize="none" size="medium" placeholder="请输入评论内容"
                                v-model="Replycomment.content" maxlength="100"></el-input>
                            <el-button type="primary" size="medium" class="butcomment"
                                @click="submitReply(Replycomment.content)">发表评论</el-button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- <el-dialog title="回复" :visible.sync="dialogFormVisible" width="30%">
                <el-form :model="Replycomment">
                    <el-form-item label="评论内容">
                        <el-input v-model="Replycomment.content" autocomplete="off" type="textarea" maxlength="100"
                            show-word-limit></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitReply(Replycomment.content)">确 定</el-button>
                </div>
            </el-dialog> -->
            <el-dialog title="" :visible.sync="dialogFormVisibleLogin" width="30%" class="backLogin">
                <p>当前尚未登录，前往<u><router-link to="/login">登录页面</router-link> </u></p>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: 'CommentView',
    props: ['foreignId', 'foreignType'],
    data() {
        return {
            // isComment:false,
            isExpanded: false,
            // maxVisible: 5,
            userAvator: '',
            comments: [],
            content: '',
            comment: {
                content: '',
            },
            Replycomment: {},
            dialogFormVisible: false,
            dialogFormVisibleLogin: false,
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
            // 敏感词字典
            sensitives: [
                "操你妈",
                "操你",
                "干你",
                "干你娘",
                "日你",
                "傻逼",
                "贱人",
                "滚蛋",
                "去死",
                "妓女",
                "混蛋",
                "tmd",
                "sb",
                "智障",
                "狗屎",
                "变态",
                "习近平",
                "江泽民",
                "胡锦涛",
                "毛泽东",
                "周恩来",
                "邓小平",
                "共产党",
                "台独",
                "藏独",
                "法轮功",
                "维吾尔",
                "伊斯兰",
                "基督教",
                "佛教",
                "清真寺",
                "伊斯兰教",
                "圣母院",
                "伏地魔",
                "赌博",
                "博彩",
                "色情",
                "色情网站",
                "裸照",
                "约炮",
                "偷窥",
                "监禁",
                "淫",
                "乳",
                "阴",
                "奸",
                "裸",
                "骚",
                "穴",
                "嫖",
                "碼",
                "陰",
                "姦",
                "枪",
                "炮",
                "肏",
                "屄",
                "弓",
                "弩",
                "氰",
                "铊",
                "氟",
                "氯",
                "胂",
                "磷",
                "毒品",
                "涉毒",
            ]

        }
    },
    created() {
        this.load()
    },
    methods: {
        expandComments() {
            this.isExpanded = true
        },
        collapseComments() {
            this.isExpanded = false
        },
        load() {
            this.request.get('/comment/findCommentAllForeign', {
                params: {
                    foreignId: this.foreignId,
                    foreignType: this.foreignType
                }
            }).then(res => {
                if (res.status === 500) {
                    this.$notify.error('系统错误')
                    return {}
                }
                console.log(res.comments[0].comment);
                this.comments = res.comments
            })
        },
        // 发表评论
        submit(content) {
            this.comment.userId = this.user.id
            this.comment.username = this.user.username
            this.comment.userAvator = this.user.avator
            this.comment.foreignId = this.foreignId
            this.comment.foreignType = this.foreignType

            // 构建正则表达式，使用敏感词列表中的词构建正则表达式模式
            const pattern = new RegExp(this.sensitives.join('|'), 'gi');
            // 使用正则表达式替换敏感词
            // this.comment.content = content.replace(pattern, '***');
            if (!this.user.username) {
                this.dialogFormVisibleLogin = true
            } else {
                // 检查文本是否包含敏感词
                if (pattern.test(content)) {
                    // 包含敏感词，不执行提交逻辑
                    this.$message.error('评论内容包含敏感词，请重新组织评论！');
                } else {
                    fetch('http://localhost:9090/comment', {
                        method: 'post',
                        headers: {
                            'Content-type': 'application/json;charset=utf-8'
                        },
                        body: JSON.stringify(this.comment)
                    }).then(res => {
                        if (res.status === 200) {
                            this.$notify.success('发表成功')
                            this.load()
                            this.comment = {}
                        }
                    })
                }
            }
        },
        // 回复评论
        submitReply(content) {
            if (!this.user.username) {
                this.dialogFormVisibleLogin = true
            } else {
                // 构建正则表达式，使用敏感词列表中的词构建正则表达式模式
                const pattern = new RegExp(this.sensitives.join('|'), 'gi');
                // 使用正则表达式替换敏感词
                // this.Replycomment.content = content.replace(pattern, '***');
                // 检查文本是否包含敏感词
                if (pattern.test(content)) {
                    // 包含敏感词，不执行提交逻辑
                    this.$message.error('评论内容包含敏感词，请重新组织评论！');
                } else {
                    fetch('http://localhost:9090/comment', {
                        method: 'post',
                        headers: {
                            'Content-type': 'application/json;charset=utf-8'
                        },
                        body: JSON.stringify(this.Replycomment)
                    }).then(res => {
                        if (res.status === 200) {
                            this.$notify.success('发表成功')
                            this.load()
                        }
                    })
                    this.Replycomment = {}
                    this.dialogFormVisible = false
                }
            }
        },
        commentReply(pid, target,item) {
            // console.log(item.comment);
            this.Replycomment = { pid: pid, userId: this.user.id, username: this.user.username, foreignId: this.foreignId, foreignType: this.foreignType, target: target, userAvator: this.user.avator }
            // this.dialogFormVisible = true
            if (pid !== null) {
                item.comment = true
            }
        },
    },
}
</script>

<style lang="less" scoped>
.activit_comment {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 40px auto 20px;
    // margin: 150px auto 20px;

    // background-color: #fff;
    img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }

    .texta {
        width: 40vw;
        margin: 0 10px;
    }

    .butcomment {
        height: 50px;
        width: 100px;
    }
}

// 评论列表
.comment_list {
    text-align: left;
    border-top: 1px solid #ccc;

    .comment_item {
        display: flex;
        // justify-content: center;
        align-items: center;

        // flex-direction: column;
        .comment_item_content {
            width: 100%;
            // height: 50px;
            margin: 0 20px;
            // background-color: #df9c9c;
            font-size: 24px;
            line-height: 35px;
            font-family: KaiTi;
            font-weight: normal;
            letter-spacing: 0em;
            color: #3D3D3D;

            border-bottom: 2px solid #ccc;

            .comment_item_content_t {
                display: flex;

                .avatorImg {
                    width: 60px;
                    height: 60px;
                    vertical-align: middle;
                    margin: 10px 20px;
                }

                .comment_item_content_c {

                    :nth-child(1) {
                        font-size: 16px;
                        color: #3D3D3D;
                    }

                    // line-height: 20px;
                    :nth-child(2) {
                        display: block;
                        color: rgb(0, 0, 0);
                    }

                    :nth-child(3) {
                        font-size: 16px;
                        color: #ccc;

                        i {
                            cursor: pointer;
                            padding: 0 10px;
                            color: #ccc;

                            &:hover {
                                color: #3D3D3D;
                                background-color: #04ffe6;
                            }
                        }
                    }

                    :nth-child(4) {
                        font-size: 16px;
                        color: #000000;
                    }
                }

            }

            .comment_reply {
                margin-left: 100px;

                .comment_reply_header {
                    .avatorImg {
                        width: 30px;
                        height: 30px;
                        vertical-align: middle;
                        margin-right: 5px;
                    }

                    span {
                        font-size: 16px;
                        color: #505050;

                        i {
                            color: #0370ff;
                        }
                    }
                }

                .commentTime {
                    font-size: 16px;
                    color: #ccc;

                    span {
                        cursor: pointer;
                        padding: 0 10px;
                        color: #ccc;

                        &:hover {
                            color: #3D3D3D;
                            background-color: #04ffe6;
                        }
                    }
                }
            }
        }
    }
}

.backLogin {
    u {
        color: #f57621;
        font-size: 24px;
    }
}
</style>