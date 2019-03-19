namespace ffs
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.richTextBox_rules = new System.Windows.Forms.RichTextBox();
            this.button_getrules = new System.Windows.Forms.Button();
            this.listView_showRules = new System.Windows.Forms.ListView();
            this.button_ff = new System.Windows.Forms.Button();
            this.listView_first = new System.Windows.Forms.ListView();
            this.listView_follow = new System.Windows.Forms.ListView();
            this.button_preview = new System.Windows.Forms.Button();
            this.listView_analysis = new System.Windows.Forms.ListView();
            this.button_goAnalysis = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.textBox_waitSentensive = new System.Windows.Forms.TextBox();
            this.button_IdentifySentense = new System.Windows.Forms.Button();
            this.listView_IdentifySentense = new System.Windows.Forms.ListView();
            this.button_Help = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // richTextBox_rules
            // 
            this.richTextBox_rules.Location = new System.Drawing.Point(13, 58);
            this.richTextBox_rules.Name = "richTextBox_rules";
            this.richTextBox_rules.Size = new System.Drawing.Size(141, 252);
            this.richTextBox_rules.TabIndex = 0;
            this.richTextBox_rules.Text = "";
            // 
            // button_getrules
            // 
            this.button_getrules.Location = new System.Drawing.Point(234, 12);
            this.button_getrules.Name = "button_getrules";
            this.button_getrules.Size = new System.Drawing.Size(100, 40);
            this.button_getrules.TabIndex = 1;
            this.button_getrules.Text = "获取规则";
            this.button_getrules.UseVisualStyleBackColor = true;
            this.button_getrules.Click += new System.EventHandler(this.button_getrules_Click);
            // 
            // listView_showRules
            // 
            this.listView_showRules.Location = new System.Drawing.Point(416, 13);
            this.listView_showRules.Name = "listView_showRules";
            this.listView_showRules.Size = new System.Drawing.Size(347, 297);
            this.listView_showRules.TabIndex = 2;
            this.listView_showRules.UseCompatibleStateImageBehavior = false;
            // 
            // button_ff
            // 
            this.button_ff.Location = new System.Drawing.Point(234, 58);
            this.button_ff.Name = "button_ff";
            this.button_ff.Size = new System.Drawing.Size(100, 40);
            this.button_ff.TabIndex = 3;
            this.button_ff.Text = "FirstFollow";
            this.button_ff.UseVisualStyleBackColor = true;
            this.button_ff.Click += new System.EventHandler(this.button_ff_Click);
            // 
            // listView_first
            // 
            this.listView_first.Location = new System.Drawing.Point(769, 12);
            this.listView_first.Name = "listView_first";
            this.listView_first.Size = new System.Drawing.Size(346, 136);
            this.listView_first.TabIndex = 4;
            this.listView_first.UseCompatibleStateImageBehavior = false;
            // 
            // listView_follow
            // 
            this.listView_follow.Location = new System.Drawing.Point(769, 165);
            this.listView_follow.Name = "listView_follow";
            this.listView_follow.Size = new System.Drawing.Size(346, 145);
            this.listView_follow.TabIndex = 5;
            this.listView_follow.UseCompatibleStateImageBehavior = false;
            // 
            // button_preview
            // 
            this.button_preview.Location = new System.Drawing.Point(234, 104);
            this.button_preview.Name = "button_preview";
            this.button_preview.Size = new System.Drawing.Size(100, 41);
            this.button_preview.TabIndex = 6;
            this.button_preview.Text = "预测分析表";
            this.button_preview.UseVisualStyleBackColor = true;
            this.button_preview.Click += new System.EventHandler(this.button_preview_Click);
            // 
            // listView_analysis
            // 
            this.listView_analysis.Location = new System.Drawing.Point(12, 319);
            this.listView_analysis.Name = "listView_analysis";
            this.listView_analysis.Size = new System.Drawing.Size(550, 284);
            this.listView_analysis.TabIndex = 7;
            this.listView_analysis.UseCompatibleStateImageBehavior = false;
            // 
            // button_goAnalysis
            // 
            this.button_goAnalysis.Location = new System.Drawing.Point(234, 151);
            this.button_goAnalysis.Name = "button_goAnalysis";
            this.button_goAnalysis.Size = new System.Drawing.Size(100, 41);
            this.button_goAnalysis.TabIndex = 8;
            this.button_goAnalysis.Text = "直达预测表";
            this.button_goAnalysis.UseVisualStyleBackColor = true;
            this.button_goAnalysis.Click += new System.EventHandler(this.button_goAnalysis_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(196, 203);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(179, 12);
            this.label1.TabIndex = 9;
            this.label1.Text = "请输入要识别的句子，请以#结尾";
            // 
            // textBox_waitSentensive
            // 
            this.textBox_waitSentensive.Location = new System.Drawing.Point(160, 224);
            this.textBox_waitSentensive.Multiline = true;
            this.textBox_waitSentensive.Name = "textBox_waitSentensive";
            this.textBox_waitSentensive.Size = new System.Drawing.Size(250, 28);
            this.textBox_waitSentensive.TabIndex = 10;
            this.textBox_waitSentensive.Text = "adccd#";
            this.textBox_waitSentensive.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            // 
            // button_IdentifySentense
            // 
            this.button_IdentifySentense.Location = new System.Drawing.Point(234, 258);
            this.button_IdentifySentense.Name = "button_IdentifySentense";
            this.button_IdentifySentense.Size = new System.Drawing.Size(100, 41);
            this.button_IdentifySentense.TabIndex = 11;
            this.button_IdentifySentense.Text = "识别句子";
            this.button_IdentifySentense.UseVisualStyleBackColor = true;
            this.button_IdentifySentense.Click += new System.EventHandler(this.button_IdentifySentense_Click);
            // 
            // listView_IdentifySentense
            // 
            this.listView_IdentifySentense.Location = new System.Drawing.Point(565, 319);
            this.listView_IdentifySentense.Name = "listView_IdentifySentense";
            this.listView_IdentifySentense.Size = new System.Drawing.Size(550, 284);
            this.listView_IdentifySentense.TabIndex = 12;
            this.listView_IdentifySentense.UseCompatibleStateImageBehavior = false;
            // 
            // button_Help
            // 
            this.button_Help.Location = new System.Drawing.Point(34, 12);
            this.button_Help.Name = "button_Help";
            this.button_Help.Size = new System.Drawing.Size(100, 40);
            this.button_Help.TabIndex = 13;
            this.button_Help.Text = "使用帮助";
            this.button_Help.UseVisualStyleBackColor = true;
            this.button_Help.Click += new System.EventHandler(this.button_Help_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.ClientSize = new System.Drawing.Size(1127, 615);
            this.Controls.Add(this.button_Help);
            this.Controls.Add(this.listView_IdentifySentense);
            this.Controls.Add(this.button_IdentifySentense);
            this.Controls.Add(this.textBox_waitSentensive);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.button_goAnalysis);
            this.Controls.Add(this.listView_analysis);
            this.Controls.Add(this.button_preview);
            this.Controls.Add(this.listView_follow);
            this.Controls.Add(this.listView_first);
            this.Controls.Add(this.button_ff);
            this.Controls.Add(this.listView_showRules);
            this.Controls.Add(this.button_getrules);
            this.Controls.Add(this.richTextBox_rules);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Form1";
            this.Text = "自定向下语法分析 Code By Chen";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.RichTextBox richTextBox_rules;
        private System.Windows.Forms.Button button_getrules;
        private System.Windows.Forms.ListView listView_showRules;
        private System.Windows.Forms.Button button_ff;
        private System.Windows.Forms.ListView listView_first;
        private System.Windows.Forms.ListView listView_follow;
        private System.Windows.Forms.Button button_preview;
        private System.Windows.Forms.ListView listView_analysis;
        private System.Windows.Forms.Button button_goAnalysis;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBox_waitSentensive;
        private System.Windows.Forms.Button button_IdentifySentense;
        private System.Windows.Forms.ListView listView_IdentifySentense;
        private System.Windows.Forms.Button button_Help;
    }
}

